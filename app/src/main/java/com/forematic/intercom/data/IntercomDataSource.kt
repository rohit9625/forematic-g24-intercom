package com.forematic.intercom.data

import com.forematic.intercom.data.database.dao.IntercomDao
import com.forematic.intercom.data.model.CallOutNumber
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

    suspend fun changeAdminNumber(newAdminNumber: String) {
        intercomDao.updateAdminNumber(newAdminNumber)
    }

    suspend fun setCallOutNumber(newCallOutNumber: String, callOutNumber: CallOutNumber) {
        when(callOutNumber) {
            CallOutNumber.FIRST -> intercomDao.updateFirstCallOutNumber(newCallOutNumber)
            CallOutNumber.SECOND -> intercomDao.updateSecondCallOutNumber(newCallOutNumber)
            CallOutNumber.THIRD -> intercomDao.updateThirdCallOutNumber(newCallOutNumber)
        }
    }

    fun getIntercomDevice(): Flow<IntercomDevice> {
        return intercomDao.getIntercomDevice().map { it.toIntercomDevice() }
    }
}