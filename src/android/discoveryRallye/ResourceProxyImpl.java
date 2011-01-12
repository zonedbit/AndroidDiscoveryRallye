package android.discoveryRallye;

import org.andnav.osm.DefaultResourceProxyImpl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

/**
 * \brief
 * Der OpenStreetMapViewItemizedOverlayWithFocus benötigt einen ResourceProxyImpl um an die Icons etc. zu kommen
 */
public class ResourceProxyImpl extends DefaultResourceProxyImpl {

	private final Context mContext;

	public ResourceProxyImpl(final Context pContext) {
		super(pContext);
		mContext = pContext;
	}

	public String getString(string pResId) {
		try {
			final int res = R.string.class.getDeclaredField(pResId.name()).getInt(null);
			return mContext.getString(res);
		} catch (final Exception e) {
			return super.getString(pResId);
		}
	}

	public Bitmap getBitmap(bitmap pResId) {
		try {
			final int res = R.drawable.class.getDeclaredField(pResId.name()).getInt(null);
			return BitmapFactory.decodeResource(mContext.getResources(), res);
		} catch (final Exception e) {
			return super.getBitmap(pResId);
		}
	}

	public Drawable getDrawable(bitmap pResId) {
		try {
			final int res = R.drawable.class.getDeclaredField(pResId.name()).getInt(null);
			return mContext.getResources().getDrawable(res);
		} catch (final Exception e) {
			return super.getDrawable(pResId);
		}
	}
}
