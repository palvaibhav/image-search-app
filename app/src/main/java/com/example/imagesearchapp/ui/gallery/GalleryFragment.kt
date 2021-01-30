package com.example.imagesearchapp.ui.gallery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.imagesearchapp.R
import com.example.imagesearchapp.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private val viewModel by viewModels<GalleryViewModel>()

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGalleryBinding.bind(view)

        val adaptor = UnsplashPhotoAdaptor()

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adaptor.withLoadStateHeaderAndFooter(
                header = UnsplashPhotoLoadStateAdapter { adaptor.retry() },
                footer = UnsplashPhotoLoadStateAdapter { adaptor.retry() }
            )
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            adaptor.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}