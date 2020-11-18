package com.myniprojects.newsi.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.myniprojects.newsi.R
import com.myniprojects.newsi.databinding.FragmentNewsBinding
import com.myniprojects.newsi.utils.openWeb
import com.myniprojects.newsi.utils.setActivityTitle
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
            webViewClient = object : WebViewClient()
            {
                override fun onPageFinished(view: WebView?, url: String?)
                {
                    super.onPageFinished(view, url)
                    if (progress == 100)
                    {
                        binding.proBar.isVisible = false
                        isVisible = true
                        Timber.d("Finished")
                    }
                }
            }

            settings.javaScriptEnabled = true

        }
        binding.webView.loadUrl(viewModel.openedNews.url)
        setActivityTitle(viewModel.openedNews.title)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar_news, menu)

        val likedItem = menu.findItem(R.id.itemLike)
        likedItem.icon = if (viewModel.openedNews.isLiked) ResourcesCompat.getDrawable(
            resources,
            R.drawable.ic_baseline_favorite_24,
            null
        )
        else ResourcesCompat.getDrawable(
            resources,
            R.drawable.ic_baseline_favorite_border_24,
            null
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId)
        {
            R.id.itemLike ->
            {
                Timber.d("Click like")
                viewModel.likeOpenedNews()

                item.icon = if (viewModel.openedNews.isLiked) ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_baseline_favorite_24,
                    null
                )
                else ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_baseline_favorite_border_24,
                    null
                )
                true
            }
            R.id.itemBrowser ->
            {
                if (!openWeb(viewModel.openedNews.url))
                {
                    Timber.d("Couldn't open web page")
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