package com.myniprojects.newsi.ui.fragments

import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.myniprojects.livesh.putBoolean
import com.myniprojects.newsi.R
import com.myniprojects.newsi.databinding.FragmentSettingsBinding
import com.myniprojects.newsi.utils.Constants.DARK_MODE_SH
import com.myniprojects.newsi.utils.Constants.HOT_NEWS_SH
import com.myniprojects.newsi.utils.Constants.OPEN_IN_EXTERNAL_SH
import com.myniprojects.newsi.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings)
{
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

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
                    .putBoolean(DARK_MODE_SH, isChecked)
                    .apply()
            }

            viewModel.openInExternal.observe(viewLifecycleOwner, {
                openInExternalBrowser.isChecked = it
            })


            openInExternalBrowser.setOnCheckedChangeListener { _, isChecked ->
                viewModel.sharedPreferences.edit()
                    .putBoolean(OPEN_IN_EXTERNAL_SH, isChecked)
                    .apply()
            }


            viewModel.showHotNews.observe(viewLifecycleOwner, {
                showHotNews.isChecked = it
            })


            showHotNews.setOnCheckedChangeListener { _, isChecked ->
                viewModel.sharedPreferences.edit()
                    .putBoolean(HOT_NEWS_SH, isChecked)
                    .apply()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar_settings, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId)
        {
            R.id.itemInfo ->
            {
                showHelpDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showHelpDialog()
    {
        val d = SpannableString(getString(R.string.info_dialog_desc))
        Linkify.addLinks(d, Linkify.ALL)

        val a = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(getString(R.string.info))
            .setMessage(d)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                // Respond to positive button press
            }
            .setIcon(R.drawable.ic_outline_info_24)
            .create()

        a.show()

        a.findViewById<TextView>(android.R.id.message)?.movementMethod = LinkMovementMethod.getInstance()
    }
}