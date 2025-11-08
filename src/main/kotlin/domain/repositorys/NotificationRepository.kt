package com.Biodex.domain.repositorys

import com.Biodex.domain.models.MarkNotificationAsRead
import com.Biodex.domain.models.NewNotification
import com.Biodex.domain.models.Notification
import com.Biodex.domain.models.NotificationWithDetails

interface NotificationRepository {
    fun findById(id: Int): Notification?
    fun allNotifications(): List<Notification>
    fun findByUser(idUser: Int): List<Notification>
    fun findUnreadByUser(idUser: Int): List<Notification>
    fun findByRequest(idRequest: Int): List<Notification>
    fun getNotificationsWithDetails(idUser: Int): List<NotificationWithDetails>
    fun createNotification(notification: NewNotification): Notification?
    fun markAsRead(id: Int, mark: MarkNotificationAsRead): Notification?
    fun markAllAsReadForUser(idUser: Int): Boolean
    fun deleteNotification(id: Int): Boolean
    fun deleteAllForUser(idUser: Int): Boolean
}