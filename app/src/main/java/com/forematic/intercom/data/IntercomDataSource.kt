package com.forematic.intercom.data

import com.forematic.intercom.data.database.dao.IntercomDao
import com.forematic.intercom.data.database.dao.RelayDao
import com.forematic.intercom.data.model.CallOutNumber
import com.forematic.intercom.data.model.IntercomDevice
import com.forematic.intercom.data.model.Relay
import com.forematic.intercom.data.model.toEntity
import com.forematic.intercom.data.model.toIntercomDevice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class IntercomDataSource(
    private val intercomDao: IntercomDao,
    private val relayDao: RelayDao
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

    suspend fun setMicVolume(newVolume: Int) {
        intercomDao.updateMicVolume(newVolume)
    }

    suspend fun setSpeakerVolume(newVolume: Int) {
        intercomDao.updateSpeakerVolume(newVolume)
    }

    suspend fun setTimezoneMode(mode: String) {
        intercomDao.updateTimezoneMode(mode)
    }

    suspend fun setupRelay(relay: Relay): Long {
        return relayDao.insertRelay(relay.toEntity())
    }

    fun getIntercomDevice(): Flow<IntercomDevice> {
        return intercomDao.getIntercomDeviceWithRelays(1).transform {
            it?.let {
                emit(it.toIntercomDevice())
            }
        }
    }
}