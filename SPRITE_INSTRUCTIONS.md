# إرشادات إضافة صورة الفانوس

## الوصف المطلوب للصورة:
"A cute, stylized 3D rendered character inspired by a magical Ramadan lantern. It has a rounded, friendly face with warm, glowing eyes. Flat shading with golden and turquoise accents. Game sprite sheet format, white background. 4 frames showing a side-view walking animation where the lantern gently bobs up and down. Clear, distinct character design, high resolution"

## خطوات إضافة الصورة:

### 1. توليد الصورة
استخدم أحد هذه الأدوات لتوليد sprite sheet:
- DALL-E 3
- Midjourney
- Stable Diffusion
- Leonardo.ai

### 2. مواصفات الصورة المطلوبة:
- **الأبعاد**: 256x64 بكسل (أو مضاعفاتها مثل 512x128، 1024x256)
- **التنسيق**: PNG مع خلفية شفافة أو بيضاء
- **عدد الإطارات**: 4 إطارات متساوية أفقياً
- **المحتوى**: فانوس رمضان بألوان ذهبية وفيروزية

### 3. إضافة الصورة للمشروع:

#### الطريقة الأولى (مستحسنة):
1. احفظ الصورة باسم `ramadan_lantern_sprite.png`
2. ضعها في المجلد: `app/src/main/res/drawable/`
3. الكود جاهز للاستخدام تلقائياً!

#### الطريقة الثانية (إذا لم يكن لديك صورة):
- التطبيق سيستخدم دوائر ملونة كبديل مؤقت

### 4. تخصيص الحجم (اختياري):
في ملف `OverlayCharacter.kt`، يمكنك تغيير حجم الشخصية:
```kotlin
.size(64.dp)  // غيّر هذا الرقم لتكبير أو تصغير الشخصية
```

### 5. تخصيص سرعة الحركة:
```kotlin
delay(200)  // غيّر هذا الرقم لتسريع أو تبطيء الأنيميشن (بالميلي ثانية)
```

## ملاحظات:
- تأكد من أن الصورة بصيغة PNG
- الخلفية الشفافة أفضل من البيضاء
- كل إطار يجب أن يكون بنفس العرض
- الصورة يجب أن تكون أفقية (عرض × ارتفاع)
