package com.example.zikey.sarparast.Helpers;

import java.util.HashMap;
import java.util.Map.Entry;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault12;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

public class NetworkTools {
  // public static final String SERVER_ADDRESS="http://89.144.182.226:88";

   public static final String METHOD_NAME="LoginPersonel";

   public static final String URL="url";

	public static String MTGNamespace = "http://www.razanpardazesh.com/";
	private static String url = "/Parseh_Mobile_Order.asmx?wsdl";

	public static Boolean isOnline(Context _context) {
		ConnectivityManager cm = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting()
				&& cm.getActiveNetworkInfo().isAvailable();
	}

	public static SoapObject CallSoapWebMethod(String serverAddress,
			String webMethodName, SoapObject request, String url)
			throws Exception {

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER12);

		envelope.setOutputSoapObject(request);
		envelope.bodyOut = request;
		envelope.dotNet = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(
				serverAddress + url);

		androidHttpTransport.call(MTGNamespace + webMethodName, envelope);

		if (envelope.bodyIn instanceof SoapObject) {
			return (SoapObject) envelope.bodyIn;
		} else {
			SoapFault12 error = null;
			if (envelope.bodyIn instanceof SoapFault12) {
				error = (SoapFault12) envelope.bodyIn;
			}
			
			String errorMg = "";
			
			if (error != null) {
				for (int i = 0; i < error.Reason.getChildCount(); i++) {
					Element e = error.Reason.getElement(i);
					for (int j = 0; j < e.getChildCount(); j++) {
						errorMg += e.getChild(j).toString();
					}
					
				}
			}
			if (TextUtils.isEmpty(errorMg)) {
				throw new MTGConnectToServerException();
			}
			
			throw new MTGConnectToServerException(errorMg);
			
		}
	}

	public static SoapObject CallSoapMethod(String serverAddress,
			String webmethodName, HashMap<String, Object> params)
			throws Exception {
		SoapObject request = new SoapObject(NetworkTools.MTGNamespace,
				webmethodName);

		for (Entry<String, Object> entry : params.entrySet()) {
			request.addProperty(entry.getKey(), entry.getValue());
		}

		return NetworkTools.CallSoapWebMethod(serverAddress, webmethodName,
				request, url);
	}

	public static String getSoapPropertyAsNullableString(SoapObject object,
			String indexName) {
		Object prop = object.getProperty(indexName);
		if (prop instanceof PropertyInfo) {
			prop = ((PropertyInfo) prop).getValue();
		}
		String output = String.valueOf(prop);
		return ((output.compareTo("null") == 0) || (output
				.compareTo("anyType{}") == 0)) ? null : output;
	}

	public static String getSoapPropertyAsNullableString(SoapObject object,
			int index) {
		Object prop = object.getProperty(index);
		if (prop instanceof PropertyInfo) {
			prop = ((PropertyInfo) prop).getValue();
		}
		String output = String.valueOf(prop);
		return ((output.compareTo("null") == 0) || (output
				.compareTo("anyType{}") == 0)) ? null : output;
	}
}
