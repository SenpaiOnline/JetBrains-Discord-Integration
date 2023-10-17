/*
 * Copyright 2017-2020 Aljoscha Grebe
 * Copyright 2023 Maxim Pavlov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package online.senpai.jetbrains.plugins.discord.plugin.notifications

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications

private val title: (String) -> String = { version -> "Discord Integration updated to $version" }
private val content = """
        Thank you for using the JetBrains Discord Integration!
        New in this version:${getChangelog()}
        """.trimIndent()

private fun getChangelog(): String = ApplicationUpdateNotification::class.java.getResource("/discord/changes.html")?.readText() ?: ""

object ApplicationUpdateNotification {
    fun show(version: String) =
        NotificationGroupManager
            .getInstance()
            .getNotificationGroup("online.senpai.jetbrains.plugins.discord.notification.update")
            .createNotification(title(version), content, NotificationType.INFORMATION)
            .run { Notifications.Bus.notify(this) }
}
