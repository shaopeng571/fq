//package com.tomato.fqsdk.control;
//
//import java.io.ByteArrayOutputStream;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//
//import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
//import com.tencent.mm.sdk.modelmsg.WXImageObject;
//import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
//import com.tencent.mm.sdk.modelmsg.WXMusicObject;
//import com.tencent.mm.sdk.modelmsg.WXTextObject;
//import com.tencent.mm.sdk.modelmsg.WXVideoObject;
//import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
//import com.tomato.fqsdk.utils.FindResHelper;
//
//public class CLWxShare {
//	/*
//	 *      ı 
//	 */
//	public void ShareText(boolean isWXSceneSession, String des) {
//		WXTextObject object = new WXTextObject();
//		object.text = des;
//		WXMediaMessage msg = new WXMediaMessage();
//		msg.mediaObject = object;
//		msg.description = des;
//		SendMessageToWX.Req req = new SendMessageToWX.Req();
//		req.transaction = "text";
//		req.message = msg;
//		req.scene = isWXSceneSession ? SendMessageToWX.Req.WXSceneSession
//				: SendMessageToWX.Req.WXSceneTimeline;
//		HySDK.mWXApi.sendReq(req);
//	}
//
//	/*
//	 *     ͼƬ
//	 */
//	public void ShareImg(Context context, boolean isWXSceneSession, String res) {
//		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
//				FindResHelper.RDrawable(res));
//		WXImageObject imgObject = new WXImageObject(bitmap);
//		WXMediaMessage msg = new WXMediaMessage();
//		msg.mediaObject = imgObject;
//		Bitmap thum = Bitmap.createScaledBitmap(bitmap, 70, 70, true);
//		bitmap.recycle();
//		msg.thumbData = Bitmap2Bytes(thum);
//		SendMessageToWX.Req req = new SendMessageToWX.Req();
//		req.transaction = "img";
//		req.message = msg;
//		req.scene = isWXSceneSession ? SendMessageToWX.Req.WXSceneSession
//				: SendMessageToWX.Req.WXSceneTimeline;
//		HySDK.mWXApi.sendReq(req);
//	}
//
//	/*
//	 *         
//	 */
//	public void ShareMusic(Context context, boolean isWXSceneSession,
//			String title, String des, String musicUrl, String res) {
//		WXMusicObject music = new WXMusicObject();
//		music.musicUrl = musicUrl;
//		WXMediaMessage msg = new WXMediaMessage();
//		msg.mediaObject = music;
//		msg.title = title;//    ֱ   
//		msg.description = des;//         
//		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
//				FindResHelper.RDrawable(res));//         ͼ
//		Bitmap thum = Bitmap.createScaledBitmap(bitmap, 70, 70, true);
//		bitmap.recycle();
//		msg.thumbData = Bitmap2Bytes(thum);
//
//		SendMessageToWX.Req req = new SendMessageToWX.Req();
//		req.transaction = "music";
//		req.message = msg;
//		req.scene = isWXSceneSession ? SendMessageToWX.Req.WXSceneSession
//				: SendMessageToWX.Req.WXSceneTimeline;
//		HySDK.mWXApi.sendReq(req);
//	}
//
//	/*
//	 *         
//	 */
//	public void ShareVideo(Context context, boolean isWXSceneSession,
//			String title, String des, String videoUrl, String res) {
//		WXVideoObject video = new WXVideoObject();
//		video.videoUrl = videoUrl;
//		WXMediaMessage msg = new WXMediaMessage();
//		msg.mediaObject = video;
//		msg.title = title;//   Ƶ    
//		msg.description = des;//   Ƶ    
//		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
//				FindResHelper.RDrawable(res));//   Ƶ    ͼ
//		Bitmap thum = Bitmap.createScaledBitmap(bitmap, 70, 70, true);
//		bitmap.recycle();
//		msg.thumbData = Bitmap2Bytes(thum);
//
//		SendMessageToWX.Req req = new SendMessageToWX.Req();
//		req.transaction = "video";
//		req.message = msg;
//		req.scene = isWXSceneSession ? SendMessageToWX.Req.WXSceneSession
//				: SendMessageToWX.Req.WXSceneTimeline;
//		HySDK.mWXApi.sendReq(req);
//	}
//
//	/*
//	 *       ַ
//	 */
//	public void ShareWebPage(Context context, boolean isWXSceneSession,
//			String title, String des, String url, String res) {
//		WXWebpageObject webpageObject = new WXWebpageObject();
//		webpageObject.webpageUrl = url;
//
//		WXMediaMessage msg = new WXMediaMessage();
//		msg.mediaObject = webpageObject;
//		msg.title = title;
//		msg.description = des;
//		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
//				FindResHelper.RDrawable(res));
//		Bitmap thum = Bitmap.createScaledBitmap(bitmap, 70, 70, true);
//		bitmap.recycle();
//		msg.thumbData = Bitmap2Bytes(thum);
//
//		SendMessageToWX.Req req = new SendMessageToWX.Req();
//		req.transaction = "webpage";
//		req.message = msg;
//		req.scene = isWXSceneSession ? SendMessageToWX.Req.WXSceneSession
//				: SendMessageToWX.Req.WXSceneTimeline;
//		HySDK.mWXApi.sendReq(req);
//	}
//
//	public byte[] Bitmap2Bytes(Bitmap bm) {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
//		return baos.toByteArray();
//	}
//}
