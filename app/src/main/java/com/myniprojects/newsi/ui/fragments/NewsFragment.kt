package com.myniprojects.newsi.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.myniprojects.newsi.R
import com.myniprojects.newsi.databinding.FragmentNewsBinding
import com.myniprojects.newsi.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news)
{

    private lateinit var binding: FragmentNewsBinding
    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsBinding.bind(view)
        with(binding.webView)
        {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
        }
        setObservers()
    }

    private fun setObservers()
    {
        viewModel.openedNews.observe(viewLifecycleOwner, {
            binding.webView.loadUrl(it.url)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar_news, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId)
        {
            R.id.itemLike ->
            {
                Timber.d("Click like")
                true
            }
            R.id.itemBrowser ->
            {
                val browserIntent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(viewModel.openedNews.value!!.url)
                    )
                if (browserIntent.resolveActivity(requireActivity().packageManager) == null)
                {
                    Timber.d("Cannot open")
                }
                else
                {
                    startActivity(browserIntent)
                }
                true
            }
            else ->
            {
                super.onOptionsItemSelected(item)
            }
        }
    }
}