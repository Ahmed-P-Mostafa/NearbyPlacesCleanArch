package com.polotika.nearbyplacescleanarch.ui.feature.restaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polotika.nearbyplacescleanarch.R
import com.polotika.nearbyplacescleanarch.databinding.FragmentRestaurantDetailsBinding
import com.polotika.nearbyplacescleanarch.domain.entity.Restaurant

private const val ARG_RESTAURANT= "restaurant"


class RestaurantDetailsFragment : Fragment() {
    private var restaurant: Restaurant? = null
    private var binding : FragmentRestaurantDetailsBinding?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            bundle.getParcelable<Restaurant>(ARG_RESTAURANT).also { restaurant = it }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestaurantDetailsBinding.inflate(inflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restaurant?.name.let {it->
            if (it!!.isEmpty())
                binding?.name?.text = NAME.plus(NA)
            else
                binding?.name?.text = NAME.plus(it)
        }

        restaurant?.address.let {
            if (it!!.isEmpty())
                binding?.address?.text = ADDRESS.plus(NA)
            else
                binding?.address?.text = ADDRESS.plus(it)
        }

        restaurant?.city.let {
            if (it!!.isEmpty())
                binding?.city?.text = CITY.plus(NA)
            else
                binding?.city?.text = CITY.plus(it)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(restaurant: Restaurant) =
            RestaurantDetailsFragment().apply {
                arguments = Bundle().apply {
                putParcelable(ARG_RESTAURANT,restaurant)
                }
            }
        const val  ADDRESS = "Address: "
        const val CITY = "City: "
        const val  NAME = "Name: "
        const val NA = "N/A"
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}