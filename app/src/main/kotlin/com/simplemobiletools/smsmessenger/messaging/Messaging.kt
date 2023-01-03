package com.simplemobiletools.smsmessenger.messaging

import android.content.Context
import android.telephony.SmsMessage
import android.widget.Toast.LENGTH_LONG
import com.klinker.android.send_message.Settings
import com.simplemobiletools.commons.extensions.showErrorToast
import com.simplemobiletools.commons.extensions.toast
import com.simplemobiletools.smsmessenger.R
import com.simplemobiletools.smsmessenger.extensions.config
import com.simplemobiletools.smsmessenger.extensions.messagingUtils
import com.simplemobiletools.smsmessenger.messaging.SmsException.Companion.EMPTY_DESTINATION_ADDRESS
import com.simplemobiletools.smsmessenger.messaging.SmsException.Companion.ERROR_PERSISTING_MESSAGE
import com.simplemobiletools.smsmessenger.messaging.SmsException.Companion.ERROR_SENDING_MESSAGE
import com.simplemobiletools.smsmessenger.models.Attachment

@Deprecated("TODO: Move/rewrite messaging config code into the app.")
fun Context.getSendMessageSettings(): Settings {
    val settings = Settings()
    settings.useSystemSending = true
    settings.deliveryReports = config.enableDeliveryReports
    settings.sendLongAsMms = config.sendLongMessageMMS
    settings.sendLongAsMmsAfter = 1
    settings.group = config.sendGroupMessageMMS
    return settings
}

fun Context.isLongMmsMessage(text: String, settings: Settings = getSendMessageSettings()): Boolean {
    val data = SmsMessage.calculateLength(text, false)
    val numPages = data.first()
    return numPages > settings.sendLongAsMmsAfter && settings.sendLongAsMms
}

/** Sends the message using the in-app SmsManager API wrappers if it's an SMS or using android-smsmms for MMS. */
fun Context.sendMessageCompat(text: String, addresses: List<String>, subId: Int?, attachments: List<Attachment>) {
    val settings = getSendMessageSettings()
    if (subId != null) {
        settings.subscriptionId = subId
    }

    val isMms = attachments.isNotEmpty() || isLongMmsMessage(text, settings) || addresses.size > 1 && settings.group
    if (isMms) {
        messagingUtils.sendMmsMessage(text, addresses, attachments, settings)
    } else {
        try {
            messagingUtils.sendSmsMessage(text, addresses.toSet(), settings.subscriptionId, settings.deliveryReports)
        } catch (e: SmsException) {
            when (e.errorCode) {
                EMPTY_DESTINATION_ADDRESS -> toast(id = R.string.empty_destination_address, length = LENGTH_LONG)
                ERROR_PERSISTING_MESSAGE -> toast(id = R.string.unable_to_save_message, length = LENGTH_LONG)
                ERROR_SENDING_MESSAGE -> toast(
                    msg = getString(R.string.unknown_error_occurred_sending_message, e.errorCode),
                    length = LENGTH_LONG
                )
            }
        } catch (e: Exception) {
            showErrorToast(e)
        }
    }
}

