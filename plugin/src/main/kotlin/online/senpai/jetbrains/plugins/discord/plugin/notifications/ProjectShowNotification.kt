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

import online.senpai.jetbrains.plugins.discord.plugin.settings.settings
import online.senpai.jetbrains.plugins.discord.plugin.settings.values.ProjectShow
import com.intellij.notification.*
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val title = "Show project in Rich Presence?"
private const val content =
    "Select if this project should be visible. You can change this later at any time under Settings > Other Settings > Discord > Project"

object ProjectShowNotification {
    suspend fun show(project: Project) = suspendCoroutine<ProjectShow> { continuation ->
        NotificationGroupManager
            .getInstance()
            .getNotificationGroup("online.senpai.jetbrains.plugins.discord.notification.project.show")
            .createNotification(title, content, NotificationType.INFORMATION)
            .apply {
                collapseDirection = Notification.CollapseActionsDirection.KEEP_LEFTMOST
                for (value in project.settings.show.selectableValues) {
                    addAction(DumbAwareAction.create(value.text) {
                        expire()
                        continuation.resume(value)
                    })
                }
            }
            .run {
                Notifications.Bus.notify(this, project)
            }
    }
}
