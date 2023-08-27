package com.aditya.googledeveloperscommunityvisualisationtool.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aditya.googledeveloperscommunityvisualisationtool.R
import com.aditya.googledeveloperscommunityvisualisationtool.databinding.FragmentAboutBinding
import de.hdodenhof.circleimageview.CircleImageView

class AboutFragment : Fragment() {

    lateinit var binding:FragmentAboutBinding
    lateinit var gmailLogoAuthor:CircleImageView
    lateinit var githubAuthor:CircleImageView
    lateinit var LinkedinAuthor:CircleImageView
    lateinit var gmailOrg:CircleImageView
    lateinit var LinkedInOrg:CircleImageView
    lateinit var twitterOrg:CircleImageView
    lateinit var GithubOrg:CircleImageView
    lateinit var googleOrg:CircleImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAboutBinding.inflate(inflater,container,false)
        val view=binding.root

        gmailLogoAuthor=binding.emailAutor
        githubAuthor=binding.githubAuthor
        LinkedinAuthor=binding.linkedInauthor
        gmailOrg=binding.gmailLogo
        LinkedInOrg=binding.linkedInLogo
        twitterOrg=binding.twitterLogo
        GithubOrg=binding.githubLogo
        googleOrg=binding.websiteLogo
        val darkshared=activity?.getSharedPreferences("Theme",Context.MODE_PRIVATE)!!
        if(darkshared.getBoolean("Night",false)){
            githubAuthor.setBackgroundResource(R.drawable.github_mark_white)
            GithubOrg.setBackgroundResource(R.drawable.github_mark_white)

        }

        gmailLogoAuthor.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, "adi54321kumar@gmail.com")
            }
            startActivity(emailIntent)
        }
        githubAuthor.setOnClickListener {
            val uri = Uri.parse("https://github.com/soCallmeAdityaKumar")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        LinkedinAuthor.setOnClickListener {
            val uri = Uri.parse("https://www.linkedin.com/in/aditya-kumar-86a039227")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        gmailOrg.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, "liquidgalaxylab@gmail.com")
            }
            startActivity(emailIntent)
        }
        twitterOrg.setOnClickListener{
            val uri = Uri.parse("https://twitter.com/_liquidgalaxy")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        GithubOrg.setOnClickListener {
            val uri = Uri.parse("https://github.com/LiquidGalaxyLAB")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        googleOrg.setOnClickListener {
            val uri = Uri.parse("https://www.liquidgalaxy.eu/")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        return view
    }


}