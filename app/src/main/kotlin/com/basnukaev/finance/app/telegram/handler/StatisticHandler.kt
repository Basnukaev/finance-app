package com.basnukaev.finance.app.telegram.handler

import com.basnukaev.finance.app.entity.User
import com.basnukaev.finance.app.moscowZoneId
import com.basnukaev.finance.app.service.ExpenseService
import com.basnukaev.finance.app.service.LocalizationService
import com.basnukaev.finance.app.service.StatisticPeriod
import com.basnukaev.finance.app.service.StatisticService
import com.basnukaev.finance.app.telegram.handler.CallbackQueryValue.STATISTIC
import com.basnukaev.finance.app.telegram.handler.CallbackQueryValue.STATISTIC_CHOSEN
import com.basnukaev.finance.app.telegram.handler.CallbackQueryValue.STATISTIC_CHOSEN_FOR_DAY
import com.basnukaev.finance.app.telegram.handler.CallbackQueryValue.STATISTIC_CHOSEN_FOR_MONTH
import com.basnukaev.finance.app.telegram.handler.CallbackQueryValue.STATISTIC_CHOSEN_FOR_WEEK
import com.basnukaev.finance.app.telegram.handler.CallbackQueryValue.STATISTIC_CHOSEN_FOR_YEAR
import com.basnukaev.finance.app.telegram.handler.CallbackQueryValue.STATISTIC_CHOSEN_SELECT_PERIOD
import com.basnukaev.finance.app.telegram.provider.CallbackQueryId
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.message.sendMessage
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter
import eu.vendeli.tgbot.types.User as TelegramUser

@Component
class StatisticHandler(
    private val localizationService: LocalizationService,
    private val expenseService: ExpenseService,
    private val statisticService: StatisticService
) {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    @CommandHandler.CallbackQuery([STATISTIC])
    suspend fun statistic(
        user: User,
        telegramUser: TelegramUser,
        bot: TelegramBot,
        callbackQueryId: CallbackQueryId,
    ) {
        sendMessage(localizationService.getMsg("statistic.select-option"))
            .inlineKeyboardMarkup {
                localizationService.getMsg("statistic.for-day").callback(STATISTIC_CHOSEN_FOR_DAY)
                newLine()
                localizationService.getMsg("statistic.for-week").callback(STATISTIC_CHOSEN_FOR_WEEK)
                newLine()
                localizationService.getMsg("statistic.for-month").callback(STATISTIC_CHOSEN_FOR_MONTH)
                newLine()
                localizationService.getMsg("statistic.for-year").callback(STATISTIC_CHOSEN_FOR_YEAR)
                newLine()
                localizationService.getMsg("statistic.select-period").callback(STATISTIC_CHOSEN_SELECT_PERIOD) // todo
            }
            .send(telegramUser, bot)
    }

    @CommandHandler.CallbackQuery([STATISTIC_CHOSEN])
    suspend fun statisticChosen(
        user: User,
        telegramUser: TelegramUser,
        bot: TelegramBot,
        callbackQueryId: CallbackQueryId,
        answer: String,
    ) {
        val (start, end) = statisticService.getStartAndEndByStatisticPeriod(StatisticPeriod.valueOf(answer))
        val statistics = expenseService.getStatisticsForPeriodByUserId(user.id!!, start, end)

        val text = localizationService.getMsg(
            messageCode = "statistic.result",
            params = arrayOf(
                start.atZone(moscowZoneId).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                end.atZone(moscowZoneId).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                statistics.totalAmount.toString(),
                statistics.mandatoryAmount.toString(),
                statistics.nonMandatoryAmount.toString(),
                statistics.categoryStats.toString()
            )
        )
        sendMessage(text)
            .send(telegramUser, bot)
    }
}