/*
 * @(#)HSLColorSpace.java
 *
 * Copyright (c) 2010 The authors and contributors of JHotDraw.
 *
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.color;

import static java.lang.Math.*;

import java.awt.color.ColorSpace;

/**
 * A HSL color space with additive complements in the hue color wheel: red is opposite cyan, magenta
 * is opposite green, blue is opposite yellow.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class HSLColorSpace extends AbstractNamedColorSpace {

  private static final long serialVersionUID = 1L;
  private static HSLColorSpace instance;

  public static HSLColorSpace getInstance() {
    if (instance == null) {
      instance = new HSLColorSpace();
    }
    return instance;
  }

  public HSLColorSpace() {
    super(ColorSpace.TYPE_HSV, 3);
  }

  @Override
  public float[] toRGB(float[] components, float[] rgb) {
    float hue = components[0];
    float saturation = components[1];
    float lightness = components[2];
    // compute p and q from saturation and lightness
    float q;
    if (lightness < 0.5f) {
      q = lightness * (1f + saturation);
    } else {
      q = lightness + saturation - (lightness * saturation);
    }
    float p = 2f * lightness - q;
    // normalize hue to -1..+1
    float hk = hue - (float) Math.floor(hue); // / 360f;
    // compute red, green and blue
    float red = hk + 1f / 3f;
    float green = hk;
    float blue = hk - 1f / 3f;
    // normalize rgb values
    if (red < 0) {
      red = red + 1f;
    } else if (red > 1) {
      red = red - 1f;
    }
    if (green < 0) {
      green = green + 1f;
    } else if (green > 1) {
      green = green - 1f;
    }
    if (blue < 0) {
      blue = blue + 1f;
    } else if (blue > 1) {
      blue = blue - 1f;
    }
    // adjust rgb values
    if (red < 1f / 6f) {
      red = p + ((q - p) * 6 * red);
    } else if (red < 0.5f) {
      red = q;
    } else if (red < 2f / 3f) {
      red = p + ((q - p) * 6 * (2f / 3f - red));
    } else {
      red = p;
    }
    if (green < 1f / 6f) {
      green = p + ((q - p) * 6 * green);
    } else if (green < 0.5f) {
      green = q;
    } else if (green < 2f / 3f) {
      green = p + ((q - p) * 6 * (2f / 3f - green));
    } else {
      green = p;
    }
    if (blue < 1f / 6f) {
      blue = p + ((q - p) * 6 * blue);
    } else if (blue < 0.5f) {
      blue = q;
    } else if (blue < 2f / 3f) {
      blue = p + ((q - p) * 6 * (2f / 3f - blue));
    } else {
      blue = p;
    }
    rgb[0] = clamp(red, 0, 1);
    rgb[1] = clamp(green, 0, 1);
    rgb[2] = clamp(blue, 0, 1);
    return rgb;
  }

  @Override
  public float[] fromRGB(float[] rgbvalue, float[] component) {
    float r = rgbvalue[0];
    float g = rgbvalue[1];
    float b = rgbvalue[2];
    float max = Math.max(Math.max(r, g), b);
    float min = Math.min(Math.min(r, g), b);
    float hue;
    float saturation;
    float luminance;
    if (max == min) {
      hue = 0;
    } else if (max == r && g >= b) {
      hue = 60f * (g - b) / (max - min);
    } else if (max == r && g < b) {
      hue = 60f * (g - b) / (max - min) + 360f;
    } else if (max == g) {
      hue = 60f * (b - r) / (max - min) + 120f;
    } else /*if (max == b)*/ {
      hue = 60f * (r - g) / (max - min) + 240f;
    }
    luminance = (max + min) / 2f;
    if (max == min) {
      saturation = 0;
    } else if (luminance <= 0.5f) {
      saturation = (max - min) / (max + min);
    } else /* if (lightness  > 0.5f)*/ {
      saturation = (max - min) / (2 - (max + min));
    }
    component[0] = hue / 360f;
    component[1] = saturation;
    component[2] = luminance;
    return component;
  }

  @Override
  public String getName(int idx) {
    switch (idx) {
      case 0:
        return "Hue";
      case 1:
        return "Saturation";
      case 2:
        return "Lightness";
      default:
        throw new IllegalArgumentException("index must be between 0 and 2:" + idx);
    }
  }

  @Override
  public float getMaxValue(int component) {
    return 1f;
  }

  @Override
  public float getMinValue(int component) {
    return 0f;
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof HSLColorSpace);
  }

  @Override
  public int hashCode() {
    return getClass().getSimpleName().hashCode();
  }

  @Override
  public String getName() {
    return "HSL";
  }

  private static float clamp(float v, float minv, float maxv) {
    return max(minv, min(v, maxv));
  }
}
