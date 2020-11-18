package com.myniprojects.newsi.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.myniprojects.newsi.R
import com.myniprojects.newsi.databinding.FragmentSettingsBinding
import com.myniprojects.newsi.utils.Constants.DARK_MODE_SH
import com.myniprojects.newsi.utils.Constants.OPEN_IN_EXTERNAL_SH
import com.myniprojects.newsi.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings)
{
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentSettingsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        setupView()
    }

    private fun setupView()
    {
        with(binding)
        {
            viewModel.darkMode.observe(viewLifecycleOwner, {
                switchDarkTheme.isChecked = it
            })


            switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
                viewModel.sharedPreferences.edit()
                    .putBoolean(DARK_MODE_SH.first, isChecked)
                    .apply()
            }

            viewModel.openInExternal.observe(viewLifecycleOwner, {
                openInExternalBrowser.isChecked = it
            })


            openInExternalBrowser.setOnCheckedChangeListener { _, isChecked ->
                viewModel.sharedPreferences.edit()
                    .putBoolean(OPEN_IN_EXTERNAL_SH.first, isChecked)
                    .apply()
            }

        }
    }
}