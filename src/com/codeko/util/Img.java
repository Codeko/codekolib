package com.codeko.util;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.awt.image.VolatileImage;

/**
 * http://www.codeko.com
 * codeko@codeko.com
 * @author Boris Burgos García.Codeko Informática 2007
 * @version 0.3
 * 12/08/2008
 **/
public class Img {

    /**
     * Devuelve una imagen escalada proporcionalmente para que quepa en un 
     * objeto dimensión. La transparencia es calculada automáticamente.
     * @param original Imagen original
     * @param encajar Tamaño máximo para la imagen.
     * @return BufferedImage
     */
    public static BufferedImage getImagenEscalada(Image original, Dimension encajar) {
        return getImagenEscalada(original, encajar, tieneTransparencias(original));
    }

    /**
     * Informa de si una imagen tiene pixeles transparentes o no
     * @param imagen Imagen a analizar
     * @return True si existen pixeles transparentes, false si no.
     */
    public static boolean tieneTransparencias(Image imagen) {
        // Extraido de http://java.cabezudo.net/?o=10&r=00057
        if (imagen instanceof BufferedImage) {
            BufferedImage bImage = (BufferedImage) imagen;
            return bImage.getColorModel().hasAlpha();
        }
        // Se utiliza un PixelGrabber para recuperar el modelo
        // de color de la imagen. Realizarlo en un solo pixel
        // por lo general es suficiente
        PixelGrabber pg = new PixelGrabber(imagen, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }

        // Obtener el modelo de color de la imagen
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }

    /**
     * Devuelve una imagen escalada proporcionalmente para que quepa en un 
     * objeto dimensión
     * @param original Imagen original
     * @param encajar Tamaño máximo para la imagen.
     * @param esTransparente true para imágenes con transparencia false para las que no la tienen
     * @return BufferedImage
     */
    public static BufferedImage getImagenEscalada(Image original, Dimension encajar, boolean esTransparente) {
        Dimension escalado = getTamanoEscalado(new Dimension(original.getWidth(null), original.getHeight(null)), encajar);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
        int transparency = Transparency.OPAQUE;
        if (esTransparente) {
            transparency = Transparency.BITMASK;
        }
        BufferedImage scaledBI = gc.createCompatibleImage((int) escalado.getWidth(), (int) escalado.getHeight(), transparency);
        Graphics2D g = scaledBI.createGraphics();
        if (esTransparente) {
            g.setComposite(AlphaComposite.Src);
        }
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(original, 0, 0, (int) escalado.getWidth(), (int) escalado.getHeight(), null);
        g.dispose();
        return scaledBI;
    }

    public static BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, boolean higherQuality) {
        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        int w, h;
        if (higherQuality) {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }

        do {
            if (higherQuality && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }
            if (higherQuality && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }
            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();
            ret = tmp;
        } while (w != targetWidth || h != targetHeight);
        return ret;
    }

    /**
     * Convierte la imagen dada a blnaco y negro.
     * No hace una conversión de gran calidad
     * @param original
     * @return BufferedImage
     */
    public static BufferedImage getImagenBlancoYNegro(BufferedImage original) {
        BufferedImageOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        BufferedImage ret = op.filter(original, null);
        op = null;
        return ret;
    }

    /**
     * Escala un objeto dimensión para que encaje dentro de otro objeto 
     * dimensión manteniendo la proporciones.
     * @param original Dimensión a encajar dentro del otro objeto
     * @param encajar Tamaño máximo que se le dará al objeto escalado.
     * @return BufferedImage
     */
    public static Dimension getTamanoEscalado(Dimension original, Dimension encajar) {
        Dimension escala = original;
        if (encajar != null) {
            escala = new Dimension(original);
            //Primero ajustamos el ancho al del hueco a encajar
            if (escala.getWidth() > encajar.getWidth()) {
                double proporcion = encajar.getWidth() / escala.getWidth();
                escala.setSize(escala.getWidth() * proporcion, escala.getHeight() * proporcion);
            }
            //Luego el alto
            if (escala.getHeight() > encajar.getHeight()) {
                double proporcion = encajar.getHeight() / escala.getHeight();
                escala.setSize(escala.getWidth() * proporcion, escala.getHeight() * proporcion);
            }
        }
        return escala;
    }

    public static BufferedImage toBufferedImage(final Image image) {
        int transparency = Transparency.OPAQUE;
        if (tieneTransparencias(image)) {
            transparency = Transparency.BITMASK;
        }
        return toBufferedImage(image, transparency);
    }

    public static BufferedImage toBufferedImage(final Image image, final int type) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        if (image instanceof VolatileImage) {
            return ((VolatileImage) image).getSnapshot();
        }
        loadImage(image);
        final BufferedImage buffImg = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        final Graphics2D g2 = buffImg.createGraphics();
        g2.drawImage(image, null, null);
        g2.dispose();
        return buffImg;
    }

    private static void loadImage(final Image image) {
        class StatusObserver implements ImageObserver {

            boolean imageLoaded = false;

            @Override
            public boolean imageUpdate(final Image img, final int infoflags,
                    final int x, final int y, final int width, final int height) {
                if (infoflags == ALLBITS) {
                    synchronized (this) {
                        imageLoaded = true;
                        notify();
                    }
                    return true;
                }
                return false;
            }
        }
        final StatusObserver imageStatus = new StatusObserver();
        synchronized (imageStatus) {
            if (image.getWidth(imageStatus) == -1 || image.getHeight(imageStatus) == -1) {
                while (!imageStatus.imageLoaded) {
                    try {
                        imageStatus.wait();
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }
    }
}
