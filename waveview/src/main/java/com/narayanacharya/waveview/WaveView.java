package com.narayanacharya.waveview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.concurrent.atomic.AtomicBoolean;

public class WaveView extends View {

    /**
     * Default values for drawing the wave
     */
    private static final int defaultNumberOfWaves = 3;
    private static final float defaultFrequency = 2.0f;
    private static final float defaultAmplitude = 0.15f;
    private static final float defaultPhaseShift = -0.05f;
    private static final float defaultDensity = 5.0f;
    private static final float defaultPrimaryLineWidth = 3.0f;
    private static final float defaultSecondaryLineWidth = 1.0f;
    private static final int defaultBackgroundColor = Color.BLACK;
    private static final int defaultWaveColor = Color.WHITE;
    private static final float defaultXAxisPositionMultiplier = 0.5f;

    /**
     * Values used for drawing the wave. Initialized to default values.
     */
    private int numberOfWaves;
    private float phase;
    private float amplitude;
    private float frequency;
    private float phaseShift;
    private float density;
    private float primaryWaveLineWidth;
    private float secondaryWaveLineWidth;
    private int backgroundColor;
    private int waveColor;
    private float xAxisPositionMultiplier;

    /**
     * Paint object for drawing the sine wave.
     */
    private Paint paint;

    /**
     * Path that defines the sine wave.
     */
    private Path path;

    /**
     * Holds state for whether the wave is in motion
     */
    private volatile AtomicBoolean isPlaying = new AtomicBoolean(true);

    public WaveView(Context context) {
        super(context);
        setUp(null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        try {
            setUp(a);
        } finally {
            a.recycle();
        }
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        try {
            setUp(a);
        } finally {
            a.recycle();
        }
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WaveView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        try {
            setUp(a);
        } finally {
            a.recycle();
        }
    }

    /**
     * Initialize the variables to default values.
     */
    private void setUp(TypedArray typedArray) {
        if (typedArray != null) {
            numberOfWaves = typedArray.getInt(R.styleable.WaveView_waveNumberOfWaves,
                    defaultNumberOfWaves);
            frequency = typedArray.getFloat(R.styleable.WaveView_waveFrequency, defaultFrequency);
            amplitude = typedArray.getFloat(R.styleable.WaveView_waveAmplitude, defaultAmplitude);
            phaseShift = typedArray.getFloat(R.styleable.WaveView_wavePhaseShift, defaultPhaseShift);
            density = typedArray.getFloat(R.styleable.WaveView_waveDensity, defaultDensity);
            primaryWaveLineWidth = typedArray.getFloat(R.styleable.WaveView_wavePrimaryLineWidth,
                    defaultPrimaryLineWidth);
            secondaryWaveLineWidth = typedArray.getFloat(R.styleable.WaveView_waveSecondaryLineWidth,
                    defaultSecondaryLineWidth);
            backgroundColor = typedArray.getColor(R.styleable.WaveView_waveBackgroundColor,
                    defaultBackgroundColor);
            waveColor = typedArray.getColor(R.styleable.WaveView_waveColor, defaultWaveColor);
            xAxisPositionMultiplier = typedArray.getFloat(R.styleable.WaveView_waveXAxisPositionMultiplier,
                    defaultXAxisPositionMultiplier);
            boundXAxisPositionMultiplier();
        } else {
            this.numberOfWaves = defaultNumberOfWaves;
            this.frequency = defaultFrequency;
            this.amplitude = defaultAmplitude;
            this.phaseShift = defaultPhaseShift;
            this.density = defaultDensity;
            this.primaryWaveLineWidth = defaultPrimaryLineWidth;
            this.secondaryWaveLineWidth = defaultSecondaryLineWidth;
            this.backgroundColor = defaultBackgroundColor;
            this.waveColor = defaultWaveColor;
            this.xAxisPositionMultiplier = defaultXAxisPositionMultiplier;
        }

        paint = new Paint();
        paint.setColor(waveColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);

        path = new Path();
    }

    /**
     * Method to restrict the xAxisPositionMultiplier to a value between 0 and 1 so that the wave
     * stays on screen.
     */
    private void boundXAxisPositionMultiplier() {
        if (xAxisPositionMultiplier < 0) {
            xAxisPositionMultiplier = 0;
        } else if (xAxisPositionMultiplier > 1) {
            xAxisPositionMultiplier = 1;
        }
    }

    public int getNumberOfWaves() {
        return numberOfWaves;
    }

    public void setNumberOfWaves(int numberOfWaves) {
        this.numberOfWaves = numberOfWaves;
    }

    public float getPhase() {
        return phase;
    }

    public void setPhase(float phase) {
        this.phase = phase;
    }

    public float getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
    }

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    public float getPhaseShift() {
        return phaseShift;
    }

    public void setPhaseShift(float phaseShift) {
        this.phaseShift = phaseShift;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getPrimaryWaveLineWidth() {
        return primaryWaveLineWidth;
    }

    public void setPrimaryWaveLineWidth(float primaryWaveLineWidth) {
        this.primaryWaveLineWidth = primaryWaveLineWidth;
    }

    public float getSecondaryWaveLineWidth() {
        return secondaryWaveLineWidth;
    }

    public void setSecondaryWaveLineWidth(float secondaryWaveLineWidth) {
        this.secondaryWaveLineWidth = secondaryWaveLineWidth;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getWaveColor() {
        return waveColor;
    }

    public void setWaveColor(int waveColor) {
        this.waveColor = waveColor;
        this.paint.setColor(waveColor);
    }

    public float getWaveXAxisPositionMultiplier() {
        return xAxisPositionMultiplier;
    }

    public void setWaveXAxisPositionMultiplier(float waveXAxisPositionMultiplier) {
        this.xAxisPositionMultiplier = waveXAxisPositionMultiplier;
        boundXAxisPositionMultiplier();
    }


    public boolean isPlaying() {
        return isPlaying.get();
    }

    public void play() {
        this.isPlaying.set(true);
    }

    public void pause() {
        this.isPlaying.set(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(backgroundColor);

        // Prepare common values
        float xAxisPosition = canvas.getHeight() * xAxisPositionMultiplier;
        float width = canvas.getWidth();
        float mid = canvas.getWidth() / 2;

        for (int i = 0; i < numberOfWaves; i++) {
            // Prepare variables for this wave
            paint.setStrokeWidth(i == 0 ? primaryWaveLineWidth : secondaryWaveLineWidth);
            float progress = 1.0f - (float) i / this.numberOfWaves;
            float normedAmplitude = (1.5f * progress - 0.5f) * this.amplitude;

            // Prepare path for this wave
            path.reset();
            float x;
            for (x = 0; x < width + density; x += density) {
                // We use a parable to scale the sinus wave, that has its peak in the middle of
                // the view.
                float scaling = (float) (-Math.pow(1 / mid * (x - mid), 2) + 1);

                float y = (float) (scaling * amplitude * normedAmplitude
                        * Math.sin(2 * Math.PI * (x / width) * frequency
                        + phase * (i + 1))
                        + xAxisPosition);

                if (x == 0) {
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }
            path.lineTo(x, canvas.getHeight());
            path.lineTo(0, canvas.getHeight());
            path.close();

            // Set opacity for this wave fill
            paint.setAlpha(i == 0 ? 255 : 255 / (i + 1));

            // Draw wave
            canvas.drawPath(path, paint);
        }

        if (isPlaying.get()) {
            this.phase += phaseShift;
        }

        invalidate();
    }
}