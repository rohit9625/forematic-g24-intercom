package com.forematic.intercom.data

import com.forematic.intercom.data.database.dao.IntercomDao
import com.forematic.intercom.data.model.IntercomDevice
import com.forematic.intercom.data.model.toEntity
import com.forematic.intercom.data.model.toIntercomDevice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class IntercomDataSource(
    private val intercomDao: IntercomDao
) {
    suspend fun setupIntercom(intercom: IntercomDevice) {
        intercomDao.insertIntercom(intercom.toEntity())
    }

    suspend fun changeProgrammingPassword(newPassword: String) {
        intercomDao.updateProgrammingPassword(newPassword)
    }

    fun getIntercomDevice(): Flow<IntercomDevice> {
        return intercomDao.getIntercomDevice().map { it.toIntercomDevice() }
    }
}