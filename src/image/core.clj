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

(def image-type->
  {BufferedImage/TYPE_3BYTE_BGR :3-byte-bgr 
   BufferedImage/TYPE_4BYTE_ABGR :4-byte-abgr 
   BufferedImage/TYPE_4BYTE_ABGR_PRE :4-byte-abgr-pre 
   BufferedImage/TYPE_BYTE_BINARY :byte-binary 
   BufferedImage/TYPE_BYTE_GRAY :byte-gray 
   BufferedImage/TYPE_BYTE_INDEXED :byte-indexed 
   BufferedImage/TYPE_CUSTOM :custom 
   BufferedImage/TYPE_INT_ARGB :int-argb 
   BufferedImage/TYPE_INT_ARGB_PRE :int-argb-pre 
   BufferedImage/TYPE_INT_BGR :int-bgr 
   BufferedImage/TYPE_INT_RGB :int-rgb 
   BufferedImage/TYPE_USHORT_555_RGB :ushort-555-rgb 
   BufferedImage/TYPE_USHORT_565_RGB :ushort-565-rgb 
   BufferedImage/TYPE_USHORT_GRAY :ushort-gray})

(def ->image-format
  {:BMP "BMP"
   :jpeg "jpeg"
   :bmp "bmp"
   :wbmp "wbmp"
   :gif "gif"
   :JPG "JPG"
   :png "png"
   :jpg "jpg"
   :JPEG "JPEG"
   :WBMP "WBMP"})

(defn load-image
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
   (new-image width height ::4-byte-abgr))
  ([width height image-type]
   (BufferedImage. width height (->image-type image-type))))

(defn get-size 
  [image]
  {:width (.getWidth image)
   :height (.getHeight image)})

(defn show-image ;; TODO util? display? view?
  [image]
  (JOptionPane/showMessageDialog
    nil
    ""
    ""
    JOptionPane/PLAIN_MESSAGE
    (ImageIcon. image)))

(defn has-alpha? ;; TODO
  [image]
  (not (nil? (.getAlphaRaster image))))

(defn image->raw-seq
  [image]
  (->> (.. image getRaster getDataBuffer getData)
       seq
       (map #(bit-and % 0xff))))

(defn raw-seq->pixel-seq
  [raw-seq image-type]
  (condp = image-type
    :3-byte-bgr
    (map (fn [[b g r]] {:r r :g g :b b}) (partition 3 raw-seq))
    :4-byte-abgr
    (map (fn [[b g r a]] {:a a :r r :g g :b b}) (partition 4 raw-seq))
    :4-byte-abgr-pre
    (map (fn [[a b g r]] {:a a :r r :g g :b b}) (partition 4 (byte-array raw-seq)))
    :byte-binary
    (byte-array raw-seq)
    :byte-gray
    (byte-array raw-seq)
    :byte-indexed
    raw-seq
    :custom
    raw-seq
    :int-argb
    (map (fn [[a r g b]] {:a a :r r :g g :b b}) (partition 4 (int-array raw-seq)))
    :int-argb-pre
    (map (fn [[a r g b]] {:a a :r r :g g :b b}) (partition 4 (int-array raw-seq)))
    :int-bgr
    (map (fn [[b g r]] {:r r :g g :b b}) (partition 3 (int-array raw-seq)))
    :int-rgb
    (map (fn [[r g b]] {:r r :g g :b b}) (partition 3 (int-array raw-seq)))
    :ushort-555-rgb
    (map (fn [[r g b]] {:r r :g g :b b}) (partition 3 (short-array raw-seq)))
    :ushort-565-rgb
    (map (fn [[r g b]] {:r r :g g :b b}) (partition 3 (short-array raw-seq)))
    :ushort-gray
    (short-array raw-seq)
    raw-seq))

(defn pixel-seq->raw-seq
  [pixel-seq image-type]
  (condp = image-type
    :3-byte-bgr
    (flatten (map (fn [{r :r g :g b :b}] [b g r]) pixel-seq))
    :4-byte-abgr
    (flatten (map (fn [{a :a r :r g :g b :b}] [b g r a]) pixel-seq))
    :4-byte-abgr-pre
    (flatten (map (fn [{a :a r :r g :g b :b}] [a b g r]) pixel-seq))
    :byte-binary
    pixel-seq
    :byte-gray
    pixel-seq
    :byte-indexed
    pixel-seq
    :custom
    pixel-seq
    :int-argb
    (flatten (map (fn [{a :a r :r g :g b :b}] [a r g b]) pixel-seq))
    :int-argb-pre
    (flatten (map (fn [{a :a r :r g :g b :b}] [a r g b]) pixel-seq))
    :int-bgr
    (flatten (map (fn [{r :r g :g b :b}] [b g r]) pixel-seq))
    :int-rgb
    (flatten (map (fn [{r :r g :g b :b}] [r g b]) pixel-seq))
    :ushort-555-rgb
    (flatten (map (fn [{r :r g :g b :b}] [r g b]) pixel-seq))
    :ushort-565-rgb
    (flatten (map (fn [{r :r g :g b :b}] [r g b]) pixel-seq))
    :ushort-gray
    pixel-seq
    pixel-seq))

(defn raw-seq->image
  [raw-seq width height image-type]
  (let [image (new-image width height image-type)]
    (.setDataElements
      (.getWritableTile image 0 0)
      0 0 width height
      (byte-array raw-seq))
    image))

(comment
  (show-image
    (raw-seq->image
      (-> "./resources/delihiros.png"
          load-image
          image->raw-seq
          (raw-seq->pixel-seq :4-byte-abgr)
          (pixel-seq->raw-seq :4-byte-abgr))
      240 240 :4-byte-abgr))

  (show-image
    (raw-seq->image
      (-> "./resources/kireso.jpeg"
          load-image
          image->raw-seq
          (raw-seq->pixel-seq (image-type-> 5))
          (pixel-seq->raw-seq (image-type-> 5)))
      459 459
      (image-type-> 5)))



      (show-image
        (raw-seq->image
          (-> "./resources/spam.jpg"
              load-image
              image->raw-seq
              (raw-seq->pixel-seq (image-type-> 5))
              (pixel-seq->raw-seq (image-type-> 5)))
          416 360 :3-byte-bgr)
        ))
