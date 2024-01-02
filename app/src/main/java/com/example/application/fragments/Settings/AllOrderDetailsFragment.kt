package com.example.application.fragments.Settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application.adapters.BillingAddressAdapter
import com.example.application.data.order.OrderStatus
import com.example.application.data.order.getOrderStatus
import com.example.application.databinding.FragmentOrderDetailBinding

class AllOrderDetailsFragment:Fragment() {

    private lateinit var binding:FragmentOrderDetailBinding
    private  val billingOrderAdapter by lazy { BillingAddressAdapter() }
    private val args by navArgs<AllOrderDetailsFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentOrderDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val order=args.order
        setUpRv()

        binding.apply {
            tvOrderId.text="Order #${order.orderId}"

            stepView.setSteps(
                mutableListOf(
                    OrderStatus.Ordered.status,
                    OrderStatus.Confirmed.status,
                    OrderStatus.Shipped.status,
                    OrderStatus.Delivered.status
                )

            )

            val currentOrderState=when(getOrderStatus(order.orderStatus)){
                is OrderStatus.Ordered->0
                is OrderStatus.Confirmed->1
                is OrderStatus.Shipped->2
                is OrderStatus.Delivered->3
                else->0
            }

            stepView.go(currentOrderState,false)

            if(currentOrderState==3){
                stepView.done(true)
            }

            tvFullName.text=order.address.name
            tvAddress.text="${order.address.street} ${order.address.city}"
            tvPhoneNumber.text=order.address.phone
            tvTotalPrice.text=order.totalPrice.toString()
        }

        billingOrderAdapter.differ.submitList(order.products)
    }

    private fun setUpRv() {
        binding.rvProducts.apply {
            adapter=billingOrderAdapter
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
    }
}