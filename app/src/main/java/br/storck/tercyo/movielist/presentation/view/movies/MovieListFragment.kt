package br.storck.tercyo.movielist.presentation.view.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.storck.tercyo.movielist.R
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.storck.tercyo.movielist.databinding.FragmentMovieListBinding
import br.storck.tercyo.movielist.presentation.viewmodel.movielist.MovieListViewModel

class MovieListFragment : Fragment() {
    private var binding: FragmentMovieListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_movie_list, container, false
        )

        return this.binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = this.activity?.let {
            ViewModelProviders.of(it)[MovieListViewModel::class.java]
        }

        this.binding?.viewModel = viewModel

        binding?.rcvwContainer?.layoutManager = LinearLayoutManager(this.context)
        binding?.rcvwContainer?.adapter = MoviesListAdapter(this.viewLifecycleOwner, viewModel)
        binding?.rcvwContainer?.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val islastItem = (recyclerView.adapter as MoviesListAdapter).islastItem

                if (islastItem) {
                    viewModel?.loadMoreMovies()
                }
            }
        })
    }
}