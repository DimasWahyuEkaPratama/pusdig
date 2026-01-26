-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 20 Jan 2026 pada 02.14
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `perpusdigital`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `buku`
--

CREATE TABLE `buku` (
  `Buku_id` int(11) NOT NULL,
  `Judul` varchar(200) NOT NULL,
  `Penulis` varchar(100) NOT NULL,
  `Penerbit` varchar(100) DEFAULT NULL,
  `Tahun_terbit` date DEFAULT NULL,
  `stok` int(11) NOT NULL DEFAULT 0,
  `kategori_id` int(11) NOT NULL,
  `imgsampul` varchar(255) DEFAULT NULL,
  `deskripsi` text DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  `update_at` date DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `created_at` date DEFAULT curdate()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `buku`
--

INSERT INTO `buku` (`Buku_id`, `Judul`, `Penulis`, `Penerbit`, `Tahun_terbit`, `stok`, `kategori_id`, `imgsampul`, `deskripsi`, `update_by`, `update_at`, `created_by`, `created_at`) VALUES
(1, 'Laskar Pelangi', 'Andrea Hirata', 'Bentang Pustaka', '2005-01-01', 5, 1, NULL, 'Novel tentang perjuangan anak-anak Belitung', NULL, NULL, 1, '2026-01-17'),
(2, 'Bumi Manusia', 'Pramoedya Ananta Toer', 'Hasta Mitra', '1980-01-01', 3, 1, NULL, 'Novel sejarah Indonesia', NULL, NULL, 1, '2026-01-17'),
(3, 'Matematika Kelas 10', 'Tim Penulis', 'Erlangga', '2020-01-01', 10, 3, NULL, 'Buku pelajaran matematika SMA', NULL, NULL, 1, '2026-01-17'),
(4, 'Fisika Kelas 11', 'Tim Penulis', 'Erlangga', '2020-01-01', 8, 3, NULL, 'Buku pelajaran fisika SMA', NULL, NULL, 1, '2026-01-17'),
(5, 'Kamus Besar Bahasa Indonesia', 'Badan Bahasa', 'Balai Pustaka', '2019-01-01', 5, 4, NULL, 'Kamus bahasa Indonesia lengkap', NULL, NULL, 1, '2026-01-17'),
(6, 'Harry Potter dan Batu Bertuah', 'J.K. Rowling', 'Gramedia', '2000-01-01', 4, 1, NULL, 'Novel fantasi anak-anak', NULL, NULL, 1, '2026-01-17'),
(7, 'Ensiklopedia Sains', 'Tim Penulis', 'Grolier', '2018-01-01', 3, 2, NULL, 'Ensiklopedia pengetahuan sains', NULL, NULL, 1, '2026-01-17'),
(8, 'Biologi Kelas 12', 'Tim Penulis', 'Erlangga', '2020-01-01', 7, 3, NULL, 'Buku pelajaran biologi SMA', NULL, NULL, 1, '2026-01-17');

-- --------------------------------------------------------

--
-- Struktur dari tabel `kategori`
--

CREATE TABLE `kategori` (
  `kategori_id` int(11) NOT NULL,
  `name_kategori` varchar(100) NOT NULL,
  `created_at` date DEFAULT curdate(),
  `created_by` int(11) DEFAULT NULL,
  `update_at` date DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `kategori`
--

INSERT INTO `kategori` (`kategori_id`, `name_kategori`, `created_at`, `created_by`, `update_at`, `update_by`) VALUES
(1, 'Fiksi', '2026-01-17', 1, NULL, NULL),
(2, 'Non-Fiksi', '2026-01-17', 1, NULL, NULL),
(3, 'Pelajaran', '2026-01-17', 1, NULL, NULL),
(4, 'Referensi', '2026-01-17', 1, NULL, NULL),
(5, 'Majalah', '2026-01-17', 1, NULL, NULL),
(6, 'Komik', '2026-01-17', 1, NULL, NULL);

-- --------------------------------------------------------

--
-- Struktur dari tabel `peminjaman`
--

CREATE TABLE `peminjaman` (
  `peminjaman_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `buku_id` int(11) NOT NULL,
  `jumlah_pinjam` int(11) NOT NULL DEFAULT 1,
  `tanggal_pinjam` date NOT NULL DEFAULT curdate(),
  `tanggal_kembali` date DEFAULT NULL,
  `Status` enum('pending','dipinjam','selesai','ditolak','pengajuan batas kembali diterima') NOT NULL DEFAULT 'pending',
  `denda` int(11) DEFAULT 0,
  `catatan` varchar(255) DEFAULT NULL,
  `pengajuan_batas_kembali` date DEFAULT NULL,
  `catatan_pengajuan` varchar(255) DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  `update_at` date DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `created_at` date DEFAULT curdate()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `peminjaman`
--

INSERT INTO `peminjaman` (`peminjaman_id`, `user_id`, `buku_id`, `jumlah_pinjam`, `tanggal_pinjam`, `tanggal_kembali`, `Status`, `denda`, `catatan`, `pengajuan_batas_kembali`, `catatan_pengajuan`, `update_by`, `update_at`, `created_by`, `created_at`) VALUES
(1, 3, 1, 1, '2026-01-10', NULL, 'dipinjam', 0, NULL, NULL, NULL, NULL, NULL, 1, '2026-01-17'),
(2, 4, 3, 1, '2026-01-12', NULL, 'dipinjam', 0, NULL, NULL, NULL, NULL, NULL, 1, '2026-01-17'),
(3, 5, 6, 1, '2026-01-15', NULL, 'pending', 0, NULL, NULL, NULL, NULL, NULL, 1, '2026-01-17');

-- --------------------------------------------------------

--
-- Struktur dari tabel `riwayat_peminjaman`
--

CREATE TABLE `riwayat_peminjaman` (
  `riwayat_id` int(11) NOT NULL,
  `peminjaman_id` int(11) NOT NULL,
  `created_at` date DEFAULT curdate()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `riwayat_peminjaman`
--

INSERT INTO `riwayat_peminjaman` (`riwayat_id`, `peminjaman_id`, `created_at`) VALUES
(1, 1, '2026-01-17'),
(2, 2, '2026-01-17'),
(3, 3, '2026-01-17');

-- --------------------------------------------------------

--
-- Struktur dari tabel `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(8) NOT NULL,
  `fullname` varchar(100) NOT NULL,
  `role` enum('admin','user') NOT NULL DEFAULT 'user',
  `status` enum('guru','siswa','lainnya') NOT NULL DEFAULT 'siswa',
  `alamat` varchar(255) DEFAULT NULL,
  `telp` varchar(15) DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  `update_at` date DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `created_at` date DEFAULT curdate()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `user`
--

INSERT INTO `user` (`user_id`, `username`, `password`, `fullname`, `role`, `status`, `alamat`, `telp`, `update_by`, `update_at`, `created_by`, `created_at`) VALUES
(1, 'admin', 'admin', 'Administrator Perpustakaan', 'admin', 'guru', 'Jl. Pendidikan No. 1', '081234567890', NULL, NULL, NULL, '2026-01-17'),
(2, 'siswa', 'siswa', 'siswa kelas XII', 'user', 'guru', 'Jl. Perpustakaan No. 2', '081234567891', NULL, NULL, 1, '2026-01-17'),
(3, 'siswa001', '3afa0d81', 'Budi Santoso', 'user', 'siswa', 'Jl. Pelajar No. 10', '081234567892', NULL, NULL, 1, '2026-01-17'),
(4, 'siswa002', '3afa0d81', 'Siti Aminah', 'user', 'siswa', 'Jl. Pendidikan No. 15', '081234567893', NULL, NULL, 1, '2026-01-17'),
(5, 'siswa003', '3afa0d81', 'Andi Wijaya', 'user', 'siswa', 'Jl. Sekolah No. 20', '081234567894', NULL, NULL, 1, '2026-01-17'),
(8, 'muji', 'bunga', 'muji dan bunga', 'admin', 'guru', 'muji suka bunga', '098684390168', NULL, NULL, NULL, '2026-01-20');

-- --------------------------------------------------------

--
-- Stand-in struktur untuk tampilan `v_buku_tersedia`
-- (Lihat di bawah untuk tampilan aktual)
--
CREATE TABLE `v_buku_tersedia` (
`Buku_id` int(11)
,`Judul` varchar(200)
,`Penulis` varchar(100)
,`Penerbit` varchar(100)
,`kategori` varchar(100)
,`stok` int(11)
,`Tahun_terbit` date
);

-- --------------------------------------------------------

--
-- Stand-in struktur untuk tampilan `v_laporan_siswa`
-- (Lihat di bawah untuk tampilan aktual)
--
CREATE TABLE `v_laporan_siswa` (
`user_id` int(11)
,`fullname` varchar(100)
,`username` varchar(50)
,`total_peminjaman` bigint(21)
,`sedang_dipinjam` decimal(22,0)
,`sudah_dikembalikan` decimal(22,0)
,`total_denda` decimal(32,0)
);

-- --------------------------------------------------------

--
-- Stand-in struktur untuk tampilan `v_peminjaman_aktif`
-- (Lihat di bawah untuk tampilan aktual)
--
CREATE TABLE `v_peminjaman_aktif` (
`peminjaman_id` int(11)
,`nama_peminjam` varchar(100)
,`username` varchar(50)
,`judul_buku` varchar(200)
,`tanggal_pinjam` date
,`tanggal_kembali` date
,`Status` enum('pending','dipinjam','selesai','ditolak','pengajuan batas kembali diterima')
,`denda` int(11)
,`lama_pinjam` int(7)
);

-- --------------------------------------------------------

--
-- Struktur untuk view `v_buku_tersedia`
--
DROP TABLE IF EXISTS `v_buku_tersedia`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_buku_tersedia`  AS SELECT `b`.`Buku_id` AS `Buku_id`, `b`.`Judul` AS `Judul`, `b`.`Penulis` AS `Penulis`, `b`.`Penerbit` AS `Penerbit`, `k`.`name_kategori` AS `kategori`, `b`.`stok` AS `stok`, `b`.`Tahun_terbit` AS `Tahun_terbit` FROM (`buku` `b` join `kategori` `k` on(`b`.`kategori_id` = `k`.`kategori_id`)) WHERE `b`.`stok` > 0 ORDER BY `b`.`Judul` ASC ;

-- --------------------------------------------------------

--
-- Struktur untuk view `v_laporan_siswa`
--
DROP TABLE IF EXISTS `v_laporan_siswa`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_laporan_siswa`  AS SELECT `u`.`user_id` AS `user_id`, `u`.`fullname` AS `fullname`, `u`.`username` AS `username`, count(`p`.`peminjaman_id`) AS `total_peminjaman`, sum(case when `p`.`Status` = 'dipinjam' then 1 else 0 end) AS `sedang_dipinjam`, sum(case when `p`.`Status` = 'selesai' then 1 else 0 end) AS `sudah_dikembalikan`, sum(`p`.`denda`) AS `total_denda` FROM (`user` `u` left join `peminjaman` `p` on(`u`.`user_id` = `p`.`user_id`)) WHERE `u`.`role` = 'user' GROUP BY `u`.`user_id`, `u`.`fullname`, `u`.`username` ;

-- --------------------------------------------------------

--
-- Struktur untuk view `v_peminjaman_aktif`
--
DROP TABLE IF EXISTS `v_peminjaman_aktif`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_peminjaman_aktif`  AS SELECT `p`.`peminjaman_id` AS `peminjaman_id`, `u`.`fullname` AS `nama_peminjam`, `u`.`username` AS `username`, `b`.`Judul` AS `judul_buku`, `p`.`tanggal_pinjam` AS `tanggal_pinjam`, `p`.`tanggal_kembali` AS `tanggal_kembali`, `p`.`Status` AS `Status`, `p`.`denda` AS `denda`, to_days(curdate()) - to_days(`p`.`tanggal_pinjam`) AS `lama_pinjam` FROM ((`peminjaman` `p` join `user` `u` on(`p`.`user_id` = `u`.`user_id`)) join `buku` `b` on(`p`.`buku_id` = `b`.`Buku_id`)) WHERE `p`.`Status` in ('pending','dipinjam','pengajuan batas kembali diterima') ;

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `buku`
--
ALTER TABLE `buku`
  ADD PRIMARY KEY (`Buku_id`),
  ADD KEY `kategori_id` (`kategori_id`),
  ADD KEY `update_by` (`update_by`),
  ADD KEY `created_by` (`created_by`);

--
-- Indeks untuk tabel `kategori`
--
ALTER TABLE `kategori`
  ADD PRIMARY KEY (`kategori_id`),
  ADD UNIQUE KEY `name_kategori` (`name_kategori`),
  ADD KEY `created_by` (`created_by`),
  ADD KEY `update_by` (`update_by`);

--
-- Indeks untuk tabel `peminjaman`
--
ALTER TABLE `peminjaman`
  ADD PRIMARY KEY (`peminjaman_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `buku_id` (`buku_id`),
  ADD KEY `update_by` (`update_by`),
  ADD KEY `created_by` (`created_by`);

--
-- Indeks untuk tabel `riwayat_peminjaman`
--
ALTER TABLE `riwayat_peminjaman`
  ADD PRIMARY KEY (`riwayat_id`),
  ADD KEY `peminjaman_id` (`peminjaman_id`);

--
-- Indeks untuk tabel `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD KEY `update_by` (`update_by`),
  ADD KEY `created_by` (`created_by`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `buku`
--
ALTER TABLE `buku`
  MODIFY `Buku_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT untuk tabel `kategori`
--
ALTER TABLE `kategori`
  MODIFY `kategori_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT untuk tabel `peminjaman`
--
ALTER TABLE `peminjaman`
  MODIFY `peminjaman_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `riwayat_peminjaman`
--
ALTER TABLE `riwayat_peminjaman`
  MODIFY `riwayat_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `buku`
--
ALTER TABLE `buku`
  ADD CONSTRAINT `buku_ibfk_1` FOREIGN KEY (`kategori_id`) REFERENCES `kategori` (`kategori_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `buku_ibfk_2` FOREIGN KEY (`update_by`) REFERENCES `user` (`user_id`) ON DELETE SET NULL,
  ADD CONSTRAINT `buku_ibfk_3` FOREIGN KEY (`created_by`) REFERENCES `user` (`user_id`) ON DELETE SET NULL;

--
-- Ketidakleluasaan untuk tabel `kategori`
--
ALTER TABLE `kategori`
  ADD CONSTRAINT `kategori_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `user` (`user_id`) ON DELETE SET NULL,
  ADD CONSTRAINT `kategori_ibfk_2` FOREIGN KEY (`update_by`) REFERENCES `user` (`user_id`) ON DELETE SET NULL;

--
-- Ketidakleluasaan untuk tabel `peminjaman`
--
ALTER TABLE `peminjaman`
  ADD CONSTRAINT `peminjaman_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `peminjaman_ibfk_2` FOREIGN KEY (`buku_id`) REFERENCES `buku` (`Buku_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `peminjaman_ibfk_3` FOREIGN KEY (`update_by`) REFERENCES `user` (`user_id`) ON DELETE SET NULL,
  ADD CONSTRAINT `peminjaman_ibfk_4` FOREIGN KEY (`created_by`) REFERENCES `user` (`user_id`) ON DELETE SET NULL;

--
-- Ketidakleluasaan untuk tabel `riwayat_peminjaman`
--
ALTER TABLE `riwayat_peminjaman`
  ADD CONSTRAINT `riwayat_peminjaman_ibfk_1` FOREIGN KEY (`peminjaman_id`) REFERENCES `peminjaman` (`peminjaman_id`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`update_by`) REFERENCES `user` (`user_id`) ON DELETE SET NULL,
  ADD CONSTRAINT `user_ibfk_2` FOREIGN KEY (`created_by`) REFERENCES `user` (`user_id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
