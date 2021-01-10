package dev.forcetower.destiny.core.task

interface BaseTask<TaskResult> {
    /**
     * Either run correctly or fails with exception
     */
    suspend fun execute(): TaskResult
}