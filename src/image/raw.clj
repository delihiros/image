(ns image.raw
  (:require [image.format :as format])
  (:import [javax.swing JOptionPane ImageIcon]
             [javax.imageio ImageIO]
             [java.io ByteArrayOutputStream ByteArrayInputStream]
             [java.awt.image BufferedImage]))

(defmulti load-image class)

(defmethod load-image
  java.lang.String
  [image-path]
  (try 
    (ImageIO/read (clojure.java.io/file image-path))
    (catch Exception e nil)))

(defmethod load-image
  java.net.URL
  [image-path]
  (try
    (ImageIO/read image-path)
    (catch Exception e nil)))

(defmethod load-image
  java.io.File
  [image-path]
  (try
    (ImageIO/read image-path)
    (catch Exception e nil)))

(defn save-image
  ([image image-path]
   (save-image image image-path :png))
  ([image image-path image-format]
   (try
     (ImageIO/write image
                    (format/image-format image-format)
                    (clojure.java.io/file image-path))
     (catch Exception e nil))))

(defn new-image
  ([width height]
   (new-image width height :4-byte-abgr))
  ([width height image-type]
   (BufferedImage. width height (format/image-type image-type))))

(defn raw-seq
  [image]
  (->> (.. image getRaster getDataBuffer getData)
       seq
       (map #(bit-and % 0xff)))) ;; [a b g r]

(defn raw-seq->image
  ([raw-seq width height]
   (raw-seq->image raw-seq width height :4-byte-abgr))
  ([raw-seq width height image-type]
   (let [image (new-image width height image-type)]
     (.setDataElements
       (.getWritableTile image 0 0)
       0 0 width height
       (->> raw-seq
            (partition 4) ;; TODO
            (map reverse)
            flatten
            byte-array))
     image)))

(defn image->4-bit-abgr
  [image]
  (let [converted
        (BufferedImage.
          (.getWidth image)
          (.getHeight image)
          (format/image-type :4-byte-abgr))]
    (.drawImage (.getGraphics converted) image 0 0 nil)
    converted))

(defn show-image ;; TODO util? display? view?
  [image]
  (JOptionPane/showMessageDialog
    nil
    ""
    ""
    JOptionPane/PLAIN_MESSAGE
    (ImageIcon. image)))


(comment
  (show-image
    (raw-seq->image
      (flatten
        (->>
          (-> "./resources/delihiros.png"
              load-image
              image->4-bit-abgr
              raw-seq)
          (partition 4)
          (map (fn [[a b g r]]
                 [a b g r])))) ;; a b g r ????
      240 240))

  (-> "./resources/delihiros.png"
      load-image
      image->4-bit-abgr
      raw-seq
      (raw-seq->image 240 240) ;; reverse ????
      show-image))
