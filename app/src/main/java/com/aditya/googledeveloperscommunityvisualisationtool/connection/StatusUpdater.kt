package com.aditya.googledeveloperscommunityvisualisationtool.connection

class StatusUpdater internal constructor(private val lgConnectionManager: LGConnectionManager) :
    Runnable {
    @Volatile
    private var cancelled = false
    override fun run() {
        try {
            while (!cancelled) {
                lgConnectionManager.tick()
                Thread.sleep(200L) //TICKS every 200ms
            }
        } catch (ignored: InterruptedException) {
        }
    }

    fun cancel() {
        cancelled = true
    }
}