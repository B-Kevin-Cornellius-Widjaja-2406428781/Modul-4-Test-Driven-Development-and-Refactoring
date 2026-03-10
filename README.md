
<details>
<summary>Refleksi Modul 4</summary>

1. Menurut saya, alur Test-Driven Development (TDD) yang telah saya ikuti sangat berguna dalam memastikan bahwa setiap bagian kode yang saya tulis memiliki tujuan yang jelas dan diuji secara menyeluruh. Dengan memulai dari penulisan tes terlebih dahulu, saya dipaksa untuk memikirkan kebutuhan fungsionalitas yang akan dikembangkan, sehingga menghasilkan kode yang lebih fokus dan terarah. Namun, saya menyadari bahwa dalam beberapa kasus, terutama saat menulis tes untuk logika bisnis yang kompleks, menulis tes terlebih dahulu bisa terasa menantang karena saya belum sepenuhnya memahami bagaimana implementasi akhir akan terlihat. Menurut saya, TDD sangat cocok dikombinasikan dengan penggunaan Generative AI untuk membantu dalam merancang tes yang lebih efektif, terutama dalam hal memprediksi edge cases atau skenario yang mungkin terlewatkan. Ke depannya, saya berencana untuk lebih sering menggunakan TDD dalam proyek-proyek saya agar dapat terus meningkatkan kualitas kode dan memastikan bahwa setiap fitur yang dikembangkan memiliki cakupan tes yang baik.


2. Menurut saya, tes yang telah saya buat sudah cukup mengikuti prinsip F.I.R.S.T. (Fast, Independent, Repeatable, Self-Validating, Timely). Prinsip Fast terpenuhi karena tes yang saya tulis dapat dijalankan dengan cepat tanpa memerlukan setup yang rumit. Independent juga tercapai karena setiap tes dirancang untuk tidak bergantung pada tes lainnya, sehingga perubahan pada satu tes tidak akan mempengaruhi hasil tes lainnya. Repeatable terpenuhi karena tes dapat dijalankan berkali-kali dengan hasil yang konsisten, tanpa dipengaruhi oleh faktor eksternal seperti urutan eksekusi atau data yang berubah. Self-Validating juga sudah saya terapkan dengan menggunakan assertion yang jelas untuk memastikan bahwa hasil tes dapat langsung dievaluasi sebagai benar atau salah. Untuk prinsip Timely, saya merasa bahwa saya sudah cukup baik dalam menulis tes pada saat yang tepat selama proses pengembangan, terutama dengan mengikuti alur TDD. Namun, saya menyadari bahwa terkadang saya bisa lebih proaktif dalam menulis tes untuk fitur yang sudah ada, terutama ketika melakukan refactoring atau penambahan fungsionalitas baru. Ke depannya, saya akan berusaha untuk lebih konsisten dalam menulis tes yang mengikuti prinsip F.I.R.S.T. untuk memastikan bahwa kode saya tetap berkualitas tinggi dan mudah di-maintain.

</details>

<details>
<summary>Refleksi Modul 3</summary>

### Penerapan Prinsip SOLID

Dalam pengerjaan proyek ini, kelima prinsip SOLID telah diterapkan untuk membangun fondasi kode yang mudah dipelihara, dan fleksibel. 

Single Responsibility Principle (SRP) diterapkan dengan memisahkan tanggung jawab setiap kelas secara jelas: *Controller* hanya menangani interaksi HTTP, *Service* mengelola logika bisnis, dan *Repository* berfokus pada akses data. 

Open/Closed Principle (OCP) diwujudkan melalui penggunaan *interface* seperti `ProductService` dan `IProductRepository`, yang memungkinkan penambahan fungsionalitas baru, misalnya, repositori berbasis database tanpa mengubah kode yang sudah ada. 

Selanjutnya, Liskov Substitution Principle (LSP) memastikan bahwa setiap implementasi konkret, seperti `ProductServiceImpl`, dapat menggantikan abstraksinya (`ProductService`) tanpa mengganggu fungsionalitas program. Sebelumnya, terdapat pelanggaran prinsip ini di mana `CarController` mewarisi fungsionalitas dari `ProductController`, yang tidak sesuai karena `Car` dan `Product` adalah entitas yang berbeda. Perbaikan dilakukan dengan memisahkan keduanya menjadi `Controller` yang independen. 

Interface Segregation Principle (ISP) diterapkan dengan membuat *interface* yang spesifik untuk setiap modul (`Product` dan `Car`), sehingga klien tidak dipaksa bergantung pada metode yang tidak mereka perlukan. 

Terakhir, Dependency Inversion Principle (DIP) diimplementasikan dengan membuat modul tingkat tinggi (seperti *Controller*) bergantung pada abstraksi (*Service interface*), bukan pada implementasi konkret, yang difasilitasi oleh mekanisme *Dependency Injection* dari Spring.

### Keuntungan Menerapkan SOLID

Penerapan prinsip SOLID membawa keuntungan signifikan dalam pengembangan proyek ini. Salah satu contoh nyata adalah kemudahan pemeliharaan yang dihasilkan dari SRP dan DIP. Ketika logika validasi produk dipindahkan dari `ProductController` ke `ProductServiceImpl`, setiap perubahan pada aturan bisnis hanya perlu dilakukan di satu tempat tanpa menyentuh lapisan *controller*. Hal ini membuat kode lebih terorganisir dan mengurangi risiko kesalahan. Selain itu, berkat OCP dan DIP, sistem menjadi sangat fleksibel. Saat ini, proyek menggunakan repositori berbasis `List` di memori, namun jika di masa depan diperlukan transisi ke database SQL, kita hanya perlu membuat implementasi `IProductRepository` yang baru tanpa harus mengubah satu baris pun kode di lapisan *service* atau *controller*. Keuntungan lainnya adalah kemudahan pengujian; dengan tanggung jawab yang terpisah, *unit test* untuk setiap komponen menjadi lebih sederhana, terisolasi, dan fokus, seperti pada `ProductControllerTest` yang hanya menguji logika HTTP dan `ProductServiceImplTest` yang fokus pada logika bisnis.

### Kerugian Tidak Menerapkan SOLID

Mengabaikan prinsip SOLID akan mengakibatkan kode yang rapuh dan sulit dikelola. Sebagai contoh, jika SRP dilanggar dengan membiarkan `ProductController` menangani logika validasi, maka setiap penambahan aturan validasi baru akan memaksa perubahan pada *controller* dan berpotensi menyebabkan duplikasi kode jika validasi yang sama dibutuhkan di tempat lain. Pelanggaran terhadap DIP, misalnya dengan `ProductController` yang membuat instance `ProductServiceImpl` secara langsung (`new ProductServiceImpl()`), akan menciptakan coupling yang ketat. Akibatnya, perubahan sekecil apa pun pada konstruktor `ProductServiceImpl` akan memicu "efek domino" yang mengharuskan modifikasi di semua kelas yang menggunakannya, membuat proses pengembangan menjadi lambat dan berisiko. Lebih lanjut, tanpa abstraksi dan *dependency injection*, pengujian menjadi sangat rumit. Menguji *controller* yang terikat erat dengan implementasi konkret akan memaksa kita untuk menyediakan seluruh rantai dependensi, mengubah *unit test* yang seharusnya cepat dan terisolasi menjadi *integration test* yang lambat dan kompleks.
</details>
<details>
<summary>Refleksi Modul 2</summary>

## Reflection 1:

Isu terkait `Utility Class Violation` dan `Class With Private Constructor Should Be Final` muncul pada kelas `EshopApplication`. PMD mendeteksi kelas tersebut hanya memiliki metode statis dan private constructor, sehingga menyarankan penggunaan modifier final. Namun, karena Spring Boot melarang kelas utama aplikasi bersifat final, saya menerapkan strategi untuk membiarkan kelas tersebut tidak bersifat final. Saya menyelesaikan konflik ini dengan menggunakan anotasi `@SuppressWarnings` untuk aturan PMD yang relevan, sehingga kualitas kode tetap terjaga tanpa melanggar ketentuan yang dibutuhkan oleh framework.

Perbaikan kode juga dilakukan pada bagian `Redundant Access Modifiers` di dalam interface `ProductService`. Dalam bahasa Java, semua metode dalam sebuah interface secara otomatis bersifat public, sehingga penulisan kata kunci public secara eksplisit dianggap redundan oleh PMD. Strategi perbaikan yang saya ambil adalah menghapus semua modifier akses tersebut pada setiap deklarasi metode. Hal ini bertujuan untuk menciptakan kode yang lebih bersih, ringkas, dan mematuhi konvensi penulisan Java modern yang menghindari sintaks yang tidak perlu.

Masalah lain yang muncul saat proses CI adalah kegagalan pengujian akibat mismatch pada penamaan file yang disebabkan oleh perbedaan konfigurasi `ignorecase` pada Git. Di lingkungan lokal, Git dikonfigurasi untuk mengabaikan perbedaan huruf kapital (case-insensitive), namun pada workflow GitHub Actions yang berbasis Linux, sistem bersifat case-sensitive. Hal ini menyebabkan file tidak ditemukan saat pengujian berjalan. Strategi penyelesaiannya adalah dengan melakukan perintah `git mv` untuk mengubah nama file secara eksplisit dari format lama ke format baru yang benar, guna memastikan konsistensi penamaan.

Isu terakhir yang ditangani mencakup aspek Security and Compliance pada repositori dan alur CI/CD. Berdasarkan rekomendasi OSSF Scorecard, saya mengidentifikasi bahwa penggunaan versi action pada GitHub Workflows (seperti @v4) memiliki risiko keamanan karena label versi tersebut bersifat mutable (dapat berubah). Strategi penyelesaiannya adalah dengan mengganti referensi versi tersebut menggunakan commit SHA yang bersifat immutable. Selain itu, saya melengkapi repositori dengan file `SECURITY.md` dan `LICENSE` untuk memastikan proyek memenuhi standar dokumentasi dan keamanan yang profesional.

## Reflection 2:

Menurut analisis saya, implementasi yang telah saya bangun sudah memenuhi kriteria Continuous Integration (CI) dan Continuous Deployment (CD) dengan baik.

Aspek Continuous Integration terpenuhi melalui rangkaian otomatisasi yang mencakup pengujian fungsional menggunakan `gradlew test` beserta pelaporan code coverage melalui JaCoCo. Selain itu, kualitas kode dipantau secara ketat menggunakan static analysis dari PMD dan OSSF Scorecard untuk security scanning. Setiap kali kode baru di-push, sistem secara otomatis memvalidasi apakah perubahan tersebut merusak fitur yang ada atau memperkenalkan celah keamanan sebelum dapat dimerge ke dalam main branch.

Aspek Continuous Deployment tercermin dari alur distribusi aplikasi yang sudah terotomatisasi sepenuhnya menggunakan Docker. Saya menerapkan strategi di mana sistem akan melakukan build dan push image ke Docker Hub terlebih dahulu, yang kemudian memicu platform deployment (dalam hal ini Fly.io) untuk melakukan pull image terbaru dan menjalankan proses deployment. Strategi penggunaan container registry pihak ketiga ini sengaja dipilih agar proses deployment tetap konsisten dan fleksibel (tidak terikat secara kaku pada satu remote repository tertentu), mengingat modul proyek yang akan berpindah-pindah repositori sepanjang semester. Alur ini memastikan code saya selalu dalam kondisi siap rilis dan meminimalkan intervensi manual, yang merupakan inti dari praktik CD yang efisien.
</details>