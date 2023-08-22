package com.aditya.googledeveloperscommunityvisualisationtool.fragments.settings

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.aditya.googledeveloperscommunityvisualisationtool.connection.LGCommand
import com.aditya.googledeveloperscommunityvisualisationtool.connection.LGConnectionManager
import com.aditya.googledeveloperscommunityvisualisationtool.create.utility.model.ActionBuildCommandUtility
import com.aditya.googledeveloperscommunityvisualisationtool.dialog.CustomDialogUtility
import com.aditya.googledeveloperscommunityvisualisationtool.utility.ConstantPrefs
import com.aditya.googledeveloperscommunityvisualisationtool.R
import com.aditya.googledeveloperscommunityvisualisationtool.databinding.FragmentLgTaskBinding


class LgTask : Fragment() {
    lateinit var binding: FragmentLgTaskBinding
    private lateinit var rebootLGButton: Button
    private lateinit var powerOffLgButton: Button
    private lateinit var cleanKmlButton: Button
    private lateinit var relaunchLgButton: Button
    private lateinit var saveKMLButton: Button
    private lateinit var resetRefreshButton: Button
    private lateinit var setRefreshButton: Button
    private lateinit var lgipAddress:String
    private lateinit var password:String
    private lateinit var lgName:String
    private lateinit var port:String
    var screenAmount=5


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentLgTaskBinding.inflate(inflater,container,false)
        val view=binding.root

        rebootLGButton=binding.RebootLg
        powerOffLgButton=binding.PowerOffLg
        cleanKmlButton=binding.CleanKML
        relaunchLgButton=binding.RelaunchLg
        saveKMLButton=binding.SaveKml
        resetRefreshButton=binding.ResetRefresh
        setRefreshButton=binding.SetRefresh

        loadSharedData()


        rebootLGButton.setOnClickListener {
            startrebootLg()
        }

        powerOffLgButton.setOnClickListener {
            startPowerOff()
        }

        cleanKmlButton.setOnClickListener {
            startCleaningKML()
        }
        relaunchLgButton.setOnClickListener {
            relaunch()
        }
        resetRefreshButton.setOnClickListener {
            resetRefresh()
        }
        setRefreshButton.setOnClickListener {
            setRefresh()
        }
        return view
    }
    private fun loadSharedData() {
        val sharedPreferences = activity?.getSharedPreferences(
            ConstantPrefs.SHARED_PREFS.name,
            Context.MODE_PRIVATE
        )
            val hostNport = sharedPreferences?.getString(ConstantPrefs.URI_TEXT.name, "")!!.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            lgipAddress=hostNport[0]
            port=hostNport[1]
            lgName = sharedPreferences.getString(ConstantPrefs.USER_NAME.name, "")!!
            password = sharedPreferences.getString(ConstantPrefs.USER_PASSWORD.name, "")!!


    }

    private fun startrebootLg(){
        val dialog = CustomDialogUtility.getDialog(requireActivity(),R.drawable.warning_popup, resources.getString(R.string.rebootLG))
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        for(i in screenAmount downTo 1){
            Log.d("reboot","trying for $i")
            try{
                val lgCommand=LGCommand(ActionBuildCommandUtility.buildRebootSlaves(password,i),LGCommand.CRITICAL_MESSAGE,object :LGCommand.Listener{
                    override fun onResponse(response: String?) {
                        dialog.dismiss()
                    }
                })
                val lgConnectionManager = LGConnectionManager.getInstance()
                lgConnectionManager!!.startConnection()
                lgConnectionManager!!.addCommandToLG(lgCommand)
            }catch (e:Exception){
                println("Could not reboot to LG")
                throw e
            }
        }
    }

    private fun setRefresh() {
        var search = "<href>##LG_PHPIFACE##kml\\/slave_{{slave}}.kml<\\/href>"
        var replace =
            "<href>##LG_PHPIFACE##kml\\/slave_{{slave}}.kml<\\/href><refreshMode>onInterval<\\/refreshMode><refreshInterval>2<\\/refreshInterval>"
        var command =
            "echo $ | sudo -S sed -i \"s/$search/$replace/\" ~/earth/kml/slave/myplaces.kml"

        var clear =
            "echo $password | sudo -S sed -i \"s/$replace/$search/\" ~/earth/kml/slave/myplaces.kml"

        for (i in 2 ..screenAmount){
            var clearCmd = clear.replace("{{slave}}", i.toString())
            var cmd = command.replace("{{slave}}", i.toString())
            var query = "sshpass -p $password ssh -t lg$i \'{{cmd}}\'"

            try {
                val firstlgCommand= LGCommand(query.replace("{{cmd}}",clearCmd),
                    LGCommand.CRITICAL_MESSAGE,object : LGCommand.Listener{
                    override fun onResponse(response: String?) {
                    }
                })
                val lgConnectionManager = LGConnectionManager.getInstance()
                lgConnectionManager!!.startConnection()
                lgConnectionManager!!.addCommandToLG(firstlgCommand)
                val secondlgCommand= LGCommand(query.replace("{{cmd}}",cmd),
                    LGCommand.CRITICAL_MESSAGE,object : LGCommand.Listener{
                    override fun onResponse(response: String?) {
                    }
                })
                lgConnectionManager!!.addCommandToLG(secondlgCommand)


            }catch(e:Exception){
                println(e)
            }

        }
        startrebootLg()

    }
    private fun resetRefresh() {
        val search = "<href>##LG_PHPIFACE##kml\\/slave_{{slave}}.kml<\\/href><refreshMode>onInterval<\\/refreshMode><refreshInterval>2<\\/refreshInterval>"
        val replace = "<href>##LG_PHPIFACE##kml\\/slave_{{slave}}.kml<\\/href>"

        val clear = "echo $password | sudo -S sed -i \"s/$search/$replace/\" ~/earth/kml/slave/myplaces.kml"

        for(i in 2..screenAmount){
            val command=clear.replace("{{slave}}",i.toString())
            val query="sshpass -p $password ssh -t lg$i '$command'"

            try {
                val lgCommand= LGCommand(query, LGCommand.CRITICAL_MESSAGE,object : LGCommand.Listener{
                    override fun onResponse(response: String?) {
                    }
                })
                val lgConnectionManager = LGConnectionManager.getInstance()
                lgConnectionManager!!.startConnection()
                lgConnectionManager!!.addCommandToLG(lgCommand)
            }catch (e:Exception){
                println(e)
            }
        }
        startrebootLg()
    }

    private fun relaunch() {
        try{
            for(i in screenAmount downTo 1){
                val secondrelaunch = "RELAUNCH_CMD="+
                        "if [ -f /etc/init/lxdm.conf ]; then\n"+
                        "export SERVICE=lxdm\n"+
                        "elif [ -f /etc/init/lightdm.conf ]; then\n"+
                        "export SERVICE=lightdm\n"+
                        "else\n"+
                        "exit 1\n"+
                        "fi\n"+
                        "if  [[ \\\$(service \\\$SERVICE status) =~ 'stop' ]]; then\n"+
                        "echo $password | sudo -S service \\\$SERVICE start\n"+
                        "else "+
                        "echo $password | sudo -S service \\\$SERVICE restart\n"+
                        "fi\n"+
                        " && sshpass -p $password ssh -x -t lg@lg$i  \"\$RELAUNCH_CMD\""
                val firstrelunch= "\"/home/$lgName/bin/lg-relaunch\" > /home/$lgName/log.txt"
                val lgConnectionManager = LGConnectionManager.getInstance()
                lgConnectionManager!!.startConnection()
                val firstRelaunch = LGCommand(
                    firstrelunch,
                    LGCommand.CRITICAL_MESSAGE, object : LGCommand.Listener {
                        override fun onResponse(response: String?) {
                        }
                    })
                lgConnectionManager.addCommandToLG(firstRelaunch)
                val secondRelaunch= LGCommand(
                    secondrelaunch,
                    LGCommand.CRITICAL_MESSAGE, object : LGCommand.Listener {
                        override fun onResponse(response: String?) {
                        }
                    })
                lgConnectionManager.addCommandToLG(secondRelaunch)

            }
        }catch (e:Exception){

        }

    }

    private fun startCleaningKML() {
        try {
            val lgConnectionManager = LGConnectionManager.getInstance()
            lgConnectionManager!!.startConnection()
            val lgCommand = LGCommand(
                ActionBuildCommandUtility.buildCommandCleanSlaves(),
                LGCommand.CRITICAL_MESSAGE, object : LGCommand.Listener {
                    override fun onResponse(response: String?) {

                    }
                })
            lgConnectionManager.addCommandToLG(lgCommand)
        }catch (e:Exception){
            println("Could not connect to LG")
        }
    }
    private fun startPowerOff() {
        val dialog = CustomDialogUtility.getDialog(requireActivity(),R.drawable.warning_popup, resources.getString(R.string.ShutdownLG))
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        for(i in screenAmount downTo 1){
            try{
                val command=
                    "sshpass -p $password ssh -t lg$i \"echo $password | sudo -S poweroff\" "
                val lgCommand= LGCommand(command, LGCommand.CRITICAL_MESSAGE,object : LGCommand.Listener{
                    override fun onResponse(response: String?) {
                        dialog.dismiss()
                    }
                })
                val lgConnectionManager = LGConnectionManager.getInstance()
                lgConnectionManager!!.startConnection()
                lgConnectionManager!!.addCommandToLG(lgCommand)
            }catch (e:Exception){
                println("Could not ShutDown to LG")
                throw e
            }
        }
    }


}