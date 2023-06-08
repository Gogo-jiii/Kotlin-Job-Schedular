package com.example.jobschedular

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.util.concurrent.Executors


class MyJobService : JobService() {

    private var context: Context? = null
    private var isJobCancelled = false
    private var intent: Intent? = null

    override fun onCreate() {
        super.onCreate()
        this.context = this
        intent = Intent("job")
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Toast.makeText(context, "Job started.", Toast.LENGTH_SHORT).show()

        val executorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executorService.execute {
            for (i in 0..49) {
                if (isJobCancelled) {
                    break
                }
                handler.post {
                    intent!!.putExtra("key", i.toString())
                    LocalBroadcastManager.getInstance(context!!).sendBroadcast(intent!!)
                }
                try {
                    Thread.sleep(5000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Toast.makeText(context, "Job cancelled.", Toast.LENGTH_SHORT).show()
        isJobCancelled = true
        return true
    }

    override fun onDestroy() {
        isJobCancelled = true
        super.onDestroy()
    }
}