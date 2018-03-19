package io.forus.me.views.me

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.journeyapps.barcodescanner.BarcodeCallback
import io.forus.me.R
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.BarcodeView
import io.forus.me.entities.base.EthereumItem


/**
 * Created by martijn.doornik on 15/03/2018.
 */
class MeFragment : Fragment() {
    private lateinit var qrListener: QrListener
    private var running: Boolean = false
    private lateinit var scanner: BarcodeView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.me_fragment, container, false)
        this.scanner = view.findViewById(R.id.qr_scanner)
        requestPermission()
        scanner.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                pauseScanner()
                if (!running) {
                    val item = EthereumItem.fromString(result.text)
                    if (item != null) {
                        qrListener.onQrResult(item)
                    } else {
                        qrListener.onQrError(QrListener.ErrorCode.INVALID_OBJECT)
                    }
                }

            }

            override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
        })
        return view
    }

    override fun onPause() {
        super.onPause()
        scanner.pause()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isEmpty()) {
            requestPermission()
        } else {
            scanner.resume()
        }
    }

    override fun onResume() {
        super.onResume()
        scanner.resume()
    }

    fun pauseScanner() {
        scanner.pause()
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.CAMERA), 0)
        }
    }

    fun resumeScanner() {
        scanner.resume()
    }

    fun with(listener: QrListener): MeFragment {
        this.qrListener = listener
        return this
    }

    interface QrListener {
        fun onQrError(code: Int)
        fun onQrResult(result: EthereumItem)
        class ErrorCode {
            companion object {
                val INVALID_OBJECT = 1
            }
        }
    }
}