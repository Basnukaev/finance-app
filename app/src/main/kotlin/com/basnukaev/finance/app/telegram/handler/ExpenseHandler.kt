package com.basnukaev.finance.app.telegram.handler

import com.basnukaev.finance.app.entity.Expense
import com.basnukaev.finance.app.entity.User
import com.basnukaev.finance.app.redis.repository.ExpenseStateKeyValueRepository
import com.basnukaev.finance.app.redis.state.ExpenseState
import com.basnukaev.finance.app.repository.CategoryRepository
import com.basnukaev.finance.app.service.ExpenseService
import com.basnukaev.finance.app.service.LocalizationService
import com.basnukaev.finance.app.service.UserService
import com.basnukaev.finance.app.telegram.annotation.CheckState
import com.basnukaev.finance.app.telegram.provider.CallbackQueryId
import eu.vendeli.ktgram.extutils.sendMessage
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.UnprocessedHandler
import eu.vendeli.tgbot.api.message.sendMessage
import eu.vendeli.tgbot.types.ParseMode
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import eu.vendeli.tgbot.types.User as TelegramUser

@Component
class ExpenseHandler(
    private val userService: UserService,
    private val expenseService: ExpenseService,
    private val localizationService: LocalizationService,
    private val expenseStateKeyValueRepository: ExpenseStateKeyValueRepository,
    private val categoryRepository: CategoryRepository,
) {

    @UnprocessedHandler
    suspend fun anyInput(
        user: User,
        telegramUser: TelegramUser,
        bot: TelegramBot,
        update: ProcessedUpdate,
    ) {
        val input = update.text
        if (input.isNotValid()) {
            bot.sendMessage(localizationService.getMsg("input.not-valid"))
                .options { parseMode = ParseMode.MarkdownV2 }
                .send(telegramUser, bot)
            return
        }

        withContext(Dispatchers.IO) {
            expenseStateKeyValueRepository.save(
                ExpenseState(
                    userId = user.id!!,
                    description = input.getDescription(),
                    amountStr = input.getAmount(),
                    comment = input.getComment(),
                )
            )
        }

        sendMessage(localizationService.getMsg("is-mandatory-expense"))
            .inlineKeyboardMarkup {
                localizationService.getMsg("yes").callback(CallbackQueryValue.USER_CHOSEN_IS_MANDATORY_YES)
                localizationService.getMsg("no").callback(CallbackQueryValue.USER_CHOSEN_IS_MANDATORY_NO)
            }
            .options { parseMode = ParseMode.MarkdownV2 }
            .send(telegramUser, bot)
    }

    @CheckState
    @CommandHandler.CallbackQuery([CallbackQueryValue.USER_CHOSEN_IS_MANDATORY])
    suspend fun userChosenIsMandatoryYes(
        user: User,
        telegramUser: TelegramUser,
        bot: TelegramBot,
        callbackQueryId: CallbackQueryId,
        answer: String,
    ) {
        val state = expenseStateKeyValueRepository.findByIdOrNull(user.id!!)!!
        if (answer == "YES") {
            state.isMandatory = true
        } else {
            state.isMandatory = false
        }

        withContext(Dispatchers.IO) {
            expenseStateKeyValueRepository.save(state)
        }

        sendMessage(localizationService.getMsg("choose-category"))
            .inlineKeyboardMarkup {
                user.categories.forEach { category ->
                    category.name.callback(CallbackQueryValue.USER_CHOSEN_CATEGORY_WITH_ID.plus(category.id))
                    newLine()
                }
            }
            .send(telegramUser, bot)
    }

    @CheckState
    @CommandHandler.CallbackQuery([CallbackQueryValue.USER_CHOSEN_CATEGORY])
    suspend fun userChosenCategory(
        user: User,
        telegramUser: TelegramUser,
        bot: TelegramBot,
        callbackQueryId: CallbackQueryId,
        categoryId: Long,
    ) {
        val state = expenseStateKeyValueRepository.findByIdOrNull(user.id!!)!!

        val expense: Expense = withContext(Dispatchers.IO) {
            expenseStateKeyValueRepository.deleteById(user.id!!)
            expenseService.addExpenseToUser(
                user = user,
                description = state.description!!,
                amountStr = state.amountStr!!,
                comment = state.comment!!,
                categoryName = categoryRepository.findByIdOrNull(categoryId)!!.name,
                isMandatory = state.isMandatory!!
            )
        }

        val text = localizationService.getMsg(
            messageCode = "expense-added",
            params = arrayOf(
                expense.category.name,
                expense.description,
                expense.amount.toString(),
                if (expense.comment.isNullOrEmpty()) "Без комментария" else expense.comment,
                if (expense.isMandatory) "Да" else "Нет"
            ),
        )
        sendMessage(text)
            .send(telegramUser, bot)
    }

    private fun String?.isNotValid(): Boolean {
        if (this == null) return true

        val lines = this.split("\n")
        return lines.size < 2 || expenseService.parseAmount(lines[1]) == null
    }

    private fun String.getDescription(): String = this.substringBefore("\n")

    private fun String.getAmount(): String {
        val lines = this.split("\n")
        return lines[1]
    }

    private fun String.getComment(): String {
        val lines = this.split("\n")
        return lines.drop(2).joinToString("\n")
    }
}