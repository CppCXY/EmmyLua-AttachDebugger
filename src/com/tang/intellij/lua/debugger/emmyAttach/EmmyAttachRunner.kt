package com.tang.intellij.lua.debugger.emmyAttach

import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.process.ProcessInfo
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.xdebugger.XDebugProcess
import com.intellij.xdebugger.XDebugProcessStarter
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebuggerManager
import com.tang.intellij.lua.debugger.LuaRunner


class EmmyAttachRunner : LuaRunner() {
    companion object {
        const val ID = "lua.emmyAttach.runner"
    }

    var configuration: EmmyAttachDebugConfiguration? = null;

    override fun getRunnerId() = ID

    override fun canRun(executorId: String, runProfile: RunProfile): Boolean {
        if (DefaultDebugExecutor.EXECUTOR_ID == executorId && runProfile is EmmyAttachDebugConfiguration) {
            configuration = runProfile
            return true
        }
        return false
    }

    override fun doExecute(state: RunProfileState, environment: ExecutionEnvironment): RunContentDescriptor {
        val manager = XDebuggerManager.getInstance(environment.project)
        val session = manager.startSession(environment, object : XDebugProcessStarter() {
            override fun start(session: XDebugSession): XDebugProcess {
                val processInfo = ProcessInfo(configuration!!.pid.toInt(),"","","")
                return EmmyAttachDebugProcess(session, processInfo)
            }
        })
        return session.runContentDescriptor
    }
}