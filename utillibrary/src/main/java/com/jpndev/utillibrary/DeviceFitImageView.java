package com.jpndev.utillibrary;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.List;


/**
 * Created by jp on 4/1/16.
 */
public class DeviceFitImageView extends ImageView {


    public static final int ASPECT_WIDTH = 0;
    public static final int ASPECT_HEIGHT = 1;

    public static final int FIXED_RATIO = 2;
    public static final int MEASUREMENT_WIDTH = 3;
    public static final int MEASUREMENT_HEIGHT = 4;
    public static final int SQUARE = 5;
    public static final int IMAGE_FIXED = 6;

    private static final float DEFAULT_ASPECT_RATIO = 1f;
    public static final int DEFAULT_PERCENTAGE_WIDTH = 20;
    public static final int DEFAULT_PERCENTAGE_HEIGHT = 20;
    public static final int DEFAULT_TOLERANCE_WIDTH = 0;
    public static final int DEFAULT_TOLERANCE_HEIGHT = 1;
    public static final int DEFAULT_BLUR_RADIUS = 0;
    private static final int DEFAULT_DOMINANT_MEASUREMENT = ASPECT_WIDTH;
    private int dominantMeasurement;
    private float aspectRatio;
    public int percentage_width, percentage_height, tolerance_width, tolerance_height,blur_radius;
    public static int past_newHeight = 0;

    public DeviceFitImageView(Context context) {
        this(context, null);
    }

    public DeviceFitImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DeviceFitImageView);
        percentage_width = a.getInt(R.styleable.DeviceFitImageView_width_percentage, DEFAULT_PERCENTAGE_WIDTH);
        percentage_height = a.getInt(R.styleable.DeviceFitImageView_height_percentage, DEFAULT_PERCENTAGE_HEIGHT);
       // a.get
        tolerance_width = a.getInt(R.styleable.DeviceFitImageView_tolerance_width, DEFAULT_TOLERANCE_WIDTH);
        tolerance_height = a.getInt(R.styleable.DeviceFitImageView_tolerance_height, DEFAULT_TOLERANCE_HEIGHT);
        dominantMeasurement = a.getInt(R.styleable.DeviceFitImageView_parameter, DEFAULT_DOMINANT_MEASUREMENT);
        aspectRatio = a.getFloat(R.styleable.DeviceFitImageView_aspect_ratio, DEFAULT_ASPECT_RATIO);
        blur_radius = a.getInt(R.styleable.DeviceFitImageView_blur_radius, DEFAULT_BLUR_RADIUS);
        a.recycle();
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    public int newWidth ;
    public int newHeight ;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(dominantMeasurement!=IMAGE_FIXED) {
            newWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            newHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        }
      //  newWidth;

        Drawable drawable = getDrawable();


        if(drawable == null) {
            setMeasuredDimension(0, 0);
        } else {
            try {
             //   if(aspectRatio)
                aspectRatio = (float) drawable.getMinimumWidth() / drawable.getMinimumHeight();
            } catch (Exception e){
               // Crashlytics.logException(e);
                aspectRatio = 1f;
            }

            switch (dominantMeasurement) {
                case SQUARE:
                    newWidth = Resources.getSystem().getDisplayMetrics().widthPixels * percentage_width / 100;
                    newWidth = newWidth - dpToPx(tolerance_width);
                    // newHeight = newWidth;
                    newHeight = newWidth;
                    break;
                case ASPECT_WIDTH:
                    newWidth = Resources.getSystem().getDisplayMetrics().widthPixels * percentage_width / 100;
                    newWidth = newWidth - dpToPx(tolerance_width);
                   // newHeight = newWidth;
                    newHeight = (int) (newWidth /aspectRatio);
                    break;

                case ASPECT_HEIGHT:

           /*         newWidth= Resources.getSystem().getDisplayMetrics().widthPixels/3;
                    newWidth=newWidth-dpToPx(tolerance_width);
                  //  int  threebywidth=newWidth/3;
                    newHeight=newWidth;*/

                    newHeight = Resources.getSystem().getDisplayMetrics().heightPixels * percentage_height / 100;
                    newHeight = newHeight - dpToPx(tolerance_height);
                   // newWidth = newHeight;
                    newWidth = (int) (newHeight * aspectRatio);
                    break;
                case FIXED_RATIO:
                    newWidth = Resources.getSystem().getDisplayMetrics().widthPixels * percentage_width / 100;
                    newWidth = newWidth - dpToPx(tolerance_width);
                    // newHeight = newWidth;
                    newHeight = Resources.getSystem().getDisplayMetrics().heightPixels * percentage_height / 100;
                    newHeight = newHeight - dpToPx(tolerance_height);
                   // newHeight = (int) (newWidth /aspectRatio);
                    break;
                case MEASUREMENT_WIDTH:
                    newWidth = getMeasuredWidth();
                    newHeight = (int) (newWidth / aspectRatio);
                 //   LogUtilsutility.LOGD("jp", " newHeight " + newHeight);
                    break;
                case MEASUREMENT_HEIGHT:
                    newHeight = getMeasuredHeight();
                    newWidth = (int) (newHeight * aspectRatio);
                 //   LogUtilsutility.LOGD("jp", " newHeight " + newHeight);
                    break;
                case IMAGE_FIXED:
                    // newHeight = heightMeasureSpec;
                    //newWidth = widthMeasureSpec;
                    LogUtilsutility.LOGD("jp", " OTHER IMAGE_FIXED " + newHeight);
                    break;
                default:
                    throw new IllegalStateException("Unknown measurement with ID " + dominantMeasurement);
            }
          //  LogUtilsutility.LOGD("jp", "dimv onMeasure  newWidth" + newWidth);
         //   LogUtilsutility.LOGD("jp", "dimv onMeasure  newHeight" + newHeight);
            setMeasuredDimension(newWidth, newHeight);
        }
    }

    public void setHeightWidth(int widthMeasureSpec, int heightMeasureSpec)
    {
        LogUtilsutility.LOGD("jp", "dimv setHeightWidth  heightMeasureSpec " + heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    public void setHeightWidthRatio(int widthMeasureSpec, int heightMeasureSpec)
    {
        LogUtilsutility.LOGD("jp", "dimv setHeightWidth  heightMeasureSpec " + heightMeasureSpec);
        double aspectRatio=(double)widthMeasureSpec/heightMeasureSpec;
        widthMeasureSpec = Resources.getSystem().getDisplayMetrics().widthPixels * percentage_width / 100;
        //  newWidth = newWidth - dpToPx(tolerance_width);
        // newHeight = newWidth;
        heightMeasureSpec = (int) (widthMeasureSpec /aspectRatio);
       /*
        if (isValid(info.height)) {
            //  device_height= info.height;
            device_height = (int) (device_width /aspectRatio);
        }*/

        //measure(newWidth, newHeight);
      //  LogUtilsutility.LOGD("layerutils", "ARL setHeightWidthRatio  newWidth " + newWidth);
      //  LogUtilsutility.LOGD("layerutils", "ARL setHeightWidthRatio  newHeight " + newHeight);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        //    if(qs_dimv!=null)
        //qs_dimv.setHeightWidth(newWidth,newHeight);
    }
    public void setAspectWidth(int pw)
    {
        int widthMeasureSpec = Resources.getSystem().getDisplayMetrics().widthPixels * pw / 100;
        LogUtilsutility.LOGD("jp", "dimv setAspectWidth  aspectRatio " + aspectRatio);
        int  heightMeasureSpec = (int) (widthMeasureSpec /aspectRatio);

        dominantMeasurement=IMAGE_FIXED;
        newWidth=widthMeasureSpec;
        newHeight=heightMeasureSpec;
        percentage_width=pw;
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        requestLayout();
        LogUtilsutility.LOGD("jp", "dimv setAspectWidth   heightMeasureSpec changed " + heightMeasureSpec);
        LogUtilsutility.LOGD("jp", "dimv setAspectWidth   widthMeasureSpec  changed" + widthMeasureSpec);

    }

    public void setPercentWidth(int widthMeasureSpec, int heightMeasureSpec,int pw)
    {
        LogUtilsutility.LOGD("jp", "dimv setPercentWidth   heightMeasureSpec " + heightMeasureSpec);
        LogUtilsutility.LOGD("jp", "dimv setPercentWidth   widthMeasureSpec " + widthMeasureSpec);
        double aspectRatio=(double)widthMeasureSpec/heightMeasureSpec;
        widthMeasureSpec = Resources.getSystem().getDisplayMetrics().widthPixels * pw / 100;
        LogUtilsutility.LOGD("jp", "dimv setPercentWidth  aspectRatio " + aspectRatio);
        heightMeasureSpec = (int) (widthMeasureSpec /aspectRatio);

        dominantMeasurement=IMAGE_FIXED;
        newWidth=widthMeasureSpec;
        newHeight=heightMeasureSpec;
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        LogUtilsutility.LOGD("jp", "dimv setPercentWidth   heightMeasureSpec changed " + heightMeasureSpec);
        LogUtilsutility.LOGD("jp", "dimv setPercentWidth   widthMeasureSpec  changed" + widthMeasureSpec);

    }



    public void setPercentHeight(int widthMeasureSpec, int heightMeasureSpec,int pw)
    {
        LogUtilsutility.LOGD("jp", "dimv setPercentWidth   heightMeasureSpec " + heightMeasureSpec);
        LogUtilsutility.LOGD("jp", "dimv setPercentWidth   widthMeasureSpec " + widthMeasureSpec);
        double aspectRatio=(double)widthMeasureSpec/heightMeasureSpec;
        heightMeasureSpec = Resources.getSystem().getDisplayMetrics().heightPixels * pw / 100;
        LogUtilsutility.LOGD("jp", "dimv setPercentWidth  aspectRatio " + aspectRatio);
        widthMeasureSpec = (int) (heightMeasureSpec *aspectRatio);

        dominantMeasurement=IMAGE_FIXED;
        newWidth=widthMeasureSpec;
        newHeight=heightMeasureSpec;
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        LogUtilsutility.LOGD("jp", "dimv setPercentWidth   heightMeasureSpec changed " + heightMeasureSpec);
        LogUtilsutility.LOGD("jp", "dimv setPercentWidth   widthMeasureSpec  changed" + widthMeasureSpec);

    }
    public void setPercentHeightWidth(int widthMeasureSpec, int heightMeasureSpec,int pw,int ph )
    {
        LogUtilsutility.LOGD("jp", "dimv setPercentWidth   heightMeasureSpec " + heightMeasureSpec);
        LogUtilsutility.LOGD("jp", "dimv setPercentWidth   widthMeasureSpec " + widthMeasureSpec);
        double aspectRatio=(double)widthMeasureSpec/heightMeasureSpec;
        heightMeasureSpec = Resources.getSystem().getDisplayMetrics().heightPixels * ph / 100;

        widthMeasureSpec = Resources.getSystem().getDisplayMetrics().widthPixels * pw / 100;
        LogUtilsutility.LOGD("jp", "dimv setPercentWidth  aspectRatio " + aspectRatio);
       // widthMeasureSpec = (int) (heightMeasureSpec *aspectRatio);

        dominantMeasurement=IMAGE_FIXED;
        newWidth=widthMeasureSpec;
        newHeight=heightMeasureSpec;
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        LogUtilsutility.LOGD("jp", "dimv setPercentWidth   heightMeasureSpec changed " + heightMeasureSpec);
        LogUtilsutility.LOGD("jp", "dimv setPercentWidth   widthMeasureSpec  changed" + widthMeasureSpec);

    }
    public void setHeightWidthParamters(int pw,int ph)
    {
        int widthMeasureSpec = Resources.getSystem().getDisplayMetrics().widthPixels * pw / 100;

        int  heightMeasureSpec =  Resources.getSystem().getDisplayMetrics().heightPixels * ph / 100;


        dominantMeasurement=IMAGE_FIXED;
        newWidth=widthMeasureSpec;
        newHeight=heightMeasureSpec;
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        LogUtilsutility.LOGD("jp", "dimv setPercentWidth   heightMeasureSpec changed " + heightMeasureSpec);
        LogUtilsutility.LOGD("jp", "dimv setPercentWidth   widthMeasureSpec  changed" + widthMeasureSpec);

    }
    /**
     * Get the dominant measurement for the aspect ratio.
     */
    public int getDominantMeasurement() {
        return dominantMeasurement;
    }

    /**
     * Set the dominant measurement for the aspect ratio.
     *
     * @see #MEASUREMENT_WIDTH
     * @see #MEASUREMENT_HEIGHT
     */
    public void setDominantMeasurement(int dominantMeasurement) {
        if(dominantMeasurement != MEASUREMENT_HEIGHT && dominantMeasurement != MEASUREMENT_WIDTH&&dominantMeasurement != ASPECT_HEIGHT && dominantMeasurement != ASPECT_WIDTH) {
            throw new IllegalArgumentException("Invalid measurement type.");
        }
        this.dominantMeasurement = dominantMeasurement;
        requestLayout();
    }


    @Override
    public void setImageDrawable(Drawable drawable) {
        if(isValid(drawable))
          if(blur_radius>0)
            {
                Bitmap bitmap =drawableToBitmap(drawable) ;
               //  Bitmap bitmap =gaussionEffect(drawableToBitmap(drawable)) ;
                if(isValid(bitmap))
                {
                    //this.setImageBitmap(bitmap);
                    Drawable drawable2 = new BitmapDrawable(getResources(), bitmap);
                    super.setImageDrawable(drawable2);
                }
                    //setImageBitmap(bitmap);
                else
                    super.setImageDrawable(drawable);
            }
         else
          super.setImageDrawable(drawable);
        else
            super.setImageDrawable(drawable);
    }

   /* @Override
    public void setImageBitmap(Bitmap bitmap) {
        if(isValid(bitmap)) {
            Bitmap bitmap = gaussionEffect(drawableToBitmap(drawable));
            setImageBitmap(bitmap);
        }
        else
            super.setImageDrawable(drawable);
        super.setImageBitmap(bm);
    }*/

  /*  @Override
    public void setBackground(Bitmap bm) {

        super.setImageBitmap(bm);
    }*/
    /*public Bitmap gaussionEffect(Bitmap photo)
    {
       // Bitmap bitmap=getDrawable();
        final RenderScript rs = RenderScript.create( getContext() );
        final Allocation input = Allocation.createFromBitmap( rs, photo, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT );
        final Allocation output = Allocation.createTyped( rs, input.getType() );
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create( rs, Element.U8_4( rs ) );
        script.setRadius( blur_radius);
        script.setInput( input );
        script.forEach( output );
        output.copyTo( photo );
        return  photo;
    }*/
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    public Boolean isValid(String text)
    {
        if(text!=null)
            if(!text.trim().equalsIgnoreCase(""))
                return  true;
        return  false;

    }
    public Boolean isValid(List list)
    {
        if(list!=null)
            if(list.size()>0)
                return  true;
        return  false;

    }
    public Boolean isValidNotNull(List list)
    {
        if(list!=null)
            // if(list.size()>0)
            return  true;
        return  false;

    }
    public Boolean isValid(Object object)
    {
        if(object!=null)
            return  true;
        return  false;

    }

    public void setAspectHeightWidth(int widthMeasureSpec, int heightMeasureSpec,int pw,int ph )
    {
        LogUtilsutility.LOGD("jp", "dimv setAspectHeightWidth   heightMeasureSpec " + heightMeasureSpec);
        LogUtilsutility.LOGD("jp", "dimv setAspectHeightWidth   widthMeasureSpec " + widthMeasureSpec);
        if(heightMeasureSpec>widthMeasureSpec)
        {
            setPercentHeight(widthMeasureSpec,heightMeasureSpec,ph);
        }
        else

        {
            setPercentWidth(widthMeasureSpec,heightMeasureSpec,pw);
        }

    }
/*
    public void setImageDrawable(@Nullable Drawable drawable) {
        if (mDrawable != drawable) {
            mResource = 0;
            mUri = null;

            final int oldWidth = mDrawableWidth;
            final int oldHeight = mDrawableHeight;

            updateDrawable(drawable);

            if (oldWidth != mDrawableWidth || oldHeight != mDrawableHeight) {
                requestLayout();
            }
            invalidate();
        }
    }*/
}