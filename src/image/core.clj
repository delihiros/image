(ns image.core
  (import [javax.swing JOptionPane ImageIcon]
          [javax.imageio ImageIO]
          [java.io ByteArrayOutputStream ByteArrayInputStream]
          [java.awt.image BufferedImage]))

(def ->image-type
  {:3-byte-bgr BufferedImage/TYPE_3BYTE_BGR
   :4-byte-abgr BufferedImage/TYPE_4BYTE_ABGR
   :4-byte-abgr-pre BufferedImage/TYPE_4BYTE_ABGR_PRE
   :byte-binary BufferedImage/TYPE_BYTE_BINARY
   :byte-gray BufferedImage/TYPE_BYTE_GRAY
   :byte-indexed BufferedImage/TYPE_BYTE_INDEXED
   :custom BufferedImage/TYPE_CUSTOM
   :int-argb BufferedImage/TYPE_INT_ARGB
   :int-argb-pre BufferedImage/TYPE_INT_ARGB_PRE
   :int-bgr BufferedImage/TYPE_INT_BGR
   :int-rgb BufferedImage/TYPE_INT_RGB
   :ushort-555-rgb BufferedImage/TYPE_USHORT_555_RGB
   :ushort-565-rgb BufferedImage/TYPE_USHORT_565_RGB
   :ushort-gray BufferedImage/TYPE_USHORT_GRAY})

(def ->image-format
  {:png "PNG"
   :jpg "JPG"
   :bmp "BMP"})

(defn read-image
  [image-path]
  (try 
    (ImageIO/read (clojure.java.io/file image-path))
    (catch Exception e nil)))

(defn save-image
  ([image image-path]
   (save-image image image-path :png))
  ([image image-path image-format]
   (try
     (ImageIO/write image (->image-format image-format) (clojure.java.io/file image-path))
     (catch Exception e nil))))

(defn new-image
  ([width height]
   (new-image width height :rgb))
  ([width height image-type]
   (BufferedImage. width height (->image-type image-type))))

(defn get-size 
  [image]
  {:width (.getWidth image)
   :height (.getHeight image)})

(defn show-image
  [image]
  (JOptionPane/showMessageDialog
    nil
    ""
    ""
    JOptionPane/PLAIN_MESSAGE
    (ImageIcon. image)))

(defn get-rgb
  [image x y]
  (.getRGB image x y))

(defn has-alpha? ;; TODO
  [image]
  (not (nil? (.getAlphaRaster image))))

(defn image->seq
  [image]
  (let [image-as-bytes
        (map #(bit-and % 0xff)
             (seq
               (.. image
                   getRaster
                   getDataBuffer
                   getData)))]
    (if (has-alpha? image) ;; TODO
      (partition 4 image-as-bytes)
      (partition 3 image-as-bytes))))

(defn seq->image
  [image-seq]
  (byte-array image-seq))

(show-image
  (let [img (new-image 240 240 :4-byte-abgr)]
    (.setDataElements
      (.getWritableTile img 0 0)
      0 0 240 240
      (-> (map (fn [[a r g b]] [b g r a]) (image->seq (read-image "./resources/delihiros.png")))
          flatten
          seq->image))
    img))
