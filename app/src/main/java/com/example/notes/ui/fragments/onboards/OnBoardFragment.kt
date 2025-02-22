package com.example.notes.ui.fragments.onboards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.notes.R
import com.example.notes.ui.adapters.OnBoardAdapter
import com.example.notes.databinding.FragmentOnBoardBinding
import com.example.notes.utils.PreferenceHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OnBoardFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardBinding
    private lateinit var sharedPreferences: PreferenceHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = PreferenceHelper()
        sharedPreferences.init(requireContext())

        if (sharedPreferences.isOnBoardingCompleted()) {
            findNavController().navigate(R.id.action_onBoardFragment_to_signInFragment)
        } else {
            initialize()
            setupListener()
            dotsIndicator()
        }
    }

    private fun initialize() {
        binding.viewpanger2.adapter = OnBoardAdapter(this)
    }

    private fun setupListener() = with(binding.viewpanger2) {
        registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 2) {
                    binding.btnStart.visibility = View.VISIBLE
                    binding.txtSkip.visibility = View.GONE
                } else {
                    binding.btnStart.visibility = View.GONE
                    binding.txtSkip.visibility = View.VISIBLE
                    binding.txtSkip.setOnClickListener {
                        if (currentItem < 2) {
                            setCurrentItem(currentItem + 2, true)
                        } else {
                            sharedPreferences.setOnBoardingCompleted(true)
                            findNavController().navigate(R.id.action_onBoardFragment_to_signInFragment)
                        }
                    }
                }
            }
        })

        binding.btnStart.setOnClickListener {
            sharedPreferences.setOnBoardingCompleted(true)
            findNavController().navigate(R.id.action_onBoardFragment_to_signInFragment)
        }
    }

    private fun dotsIndicator() = with(binding.viewpanger2) {
        TabLayoutMediator(binding.tabLayout, binding.viewpanger2) { tab, position -> }.attach()
        registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDotsIndicator(binding.tabLayout, position)
            }
        })
        updateDotsIndicator(binding.tabLayout, 0)
    }

    private fun updateDotsIndicator(tabLayout: TabLayout, selectedPosition: Int) {
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if (i == selectedPosition) {
                tab?.setIcon(R.drawable.indicator)
            } else {
                tab?.setIcon(R.drawable.indicator2)
            }
        }
    }
}
