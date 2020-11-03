package com.myniprojects.newsi.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.myniprojects.newsi.R
import com.myniprojects.newsi.databinding.FragmentSettingsBinding
import com.myniprojects.newsi.utils.Constants.KEY_DARK_MODE
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
            viewModel.liveSharedPreferences
                .getBoolean(KEY_DARK_MODE, false)
                .observe(viewLifecycleOwner, {
                    Timber.d("Dark theme in settings $it")

                    switchDarkTheme.isChecked = it
                })

            switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
                Timber.d("Changed to $isChecked")
                viewModel.liveSharedPreferences.preferences.edit()
                    .putBoolean(KEY_DARK_MODE, isChecked)
                    .apply()
            }
        }
    }
}