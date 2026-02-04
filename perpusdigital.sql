-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 04, 2026 at 10:02 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

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
-- Table structure for table `buku`
--

CREATE TABLE `buku` (
  `Buku_id` int(11) NOT NULL,
  `Judul` varchar(200) NOT NULL,
  `Penulis` varchar(100) NOT NULL,
  `Penerbit` varchar(100) DEFAULT NULL,
  `Tahun_terbit` date DEFAULT NULL,
  `stok` int(11) NOT NULL DEFAULT 0,
  `kategori_id` int(11) NOT NULL,
  `rak_buku` varchar(255) NOT NULL,
  `imgsampul` varchar(255) DEFAULT NULL,
  `deskripsi` text DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `created_by` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `buku`
--

INSERT INTO `buku` (`Buku_id`, `Judul`, `Penulis`, `Penerbit`, `Tahun_terbit`, `stok`, `kategori_id`, `rak_buku`, `imgsampul`, `deskripsi`, `update_by`, `update_at`, `created_by`, `created_at`) VALUES
(25, 'Testing', 'Testing', 'Testing', '2026-02-02', 17, 10, 'Testing', 'C:\\Users\\HP\\Downloads\\Coursel 1.png', 'Testing', 14, '2026-02-04 07:36:39', NULL, '2026-02-02 03:59:28'),
(26, 'TESTIING', 'TESTIING', 'TESTIING', '2026-02-02', 21, 8, 'TESTIING', 'C:\\Users\\ASUS vivobook\\OneDrive\\ドキュメント\\Aston.jpg', 'TESTIING', 14, '2026-02-04 02:05:52', 14, '2026-02-02 07:53:47');

-- --------------------------------------------------------

--
-- Table structure for table `buku_item`
--

CREATE TABLE `buku_item` (
  `bukuitem_id` int(11) NOT NULL,
  `buku_id` int(11) NOT NULL,
  `kode_buku` varchar(20) NOT NULL,
  `status` enum('tersedia','dipinjam','rusak','hilang') NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `update_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `buku_item`
--

INSERT INTO `buku_item` (`bukuitem_id`, `buku_id`, `kode_buku`, `status`, `created_at`, `update_at`) VALUES
(11, 25, 'BK-0001', 'tersedia', '2026-02-01 17:00:00', '2026-02-02 03:59:28'),
(12, 25, 'BK-0002', 'tersedia', '2026-02-01 17:00:00', '2026-02-02 03:59:28'),
(13, 26, 'BK-0003', 'tersedia', '2026-02-01 17:00:00', '2026-02-02 07:53:47'),
(14, 25, 'N/A', '', '2026-02-01 17:00:00', '2026-02-04 06:40:54');

-- --------------------------------------------------------

--
-- Table structure for table `kategori`
--

CREATE TABLE `kategori` (
  `kategori_id` int(11) NOT NULL,
  `name_kategori` varchar(100) NOT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `created_by` int(11) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `update_by` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kategori`
--

INSERT INTO `kategori` (`kategori_id`, `name_kategori`, `created_at`, `created_by`, `update_at`, `update_by`) VALUES
(1, 'Fiksi', '2026-01-16 17:00:00', NULL, NULL, NULL),
(2, 'Non-Fiksi', '2026-01-16 17:00:00', NULL, NULL, NULL),
(4, 'Referensi', '2026-01-16 17:00:00', NULL, NULL, NULL),
(5, 'Makalah', '2026-01-16 17:00:00', NULL, '2026-01-28 01:53:22', 15),
(8, 'IPAS', '2026-01-15 17:00:00', NULL, NULL, NULL),
(9, 'Matematik', '2026-01-26 17:00:00', NULL, '2026-01-28 01:53:38', 15),
(10, 'MTK', '2026-01-26 17:00:00', NULL, NULL, NULL),
(11, 'test', '2026-01-28 00:47:55', 14, '2026-01-28 00:47:55', 14);

-- --------------------------------------------------------

--
-- Table structure for table `peminjaman`
--

CREATE TABLE `peminjaman` (
  `peminjaman_id` int(11) NOT NULL,
  `kode_peminjaman` varchar(20) NOT NULL,
  `user_id` int(11) NOT NULL,
  `buku_id` int(11) NOT NULL,
  `kd_bk1` varchar(9) DEFAULT NULL,
  `kd_bk2` varchar(9) DEFAULT NULL,
  `kd_bk3` varchar(9) DEFAULT NULL,
  `jumlah_pinjam` int(11) NOT NULL DEFAULT 1,
  `tanggal_pinjam` date NOT NULL DEFAULT curdate(),
  `tanggal_kembali` date DEFAULT NULL,
  `Status` enum('pending','dipinjam','selesai','ditolak','pengajuan batas kembali diterima') NOT NULL DEFAULT 'pending',
  `denda` int(11) DEFAULT 0,
  `bayar` int(255) DEFAULT NULL,
  `kembali` int(255) DEFAULT NULL,
  `total` int(255) DEFAULT NULL,
  `catatan` varchar(255) DEFAULT NULL,
  `pengajuan_batas_kembali` date DEFAULT NULL,
  `catatan_pengajuan` varchar(255) DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `created_by` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `peminjaman`
--

INSERT INTO `peminjaman` (`peminjaman_id`, `kode_peminjaman`, `user_id`, `buku_id`, `kd_bk1`, `kd_bk2`, `kd_bk3`, `jumlah_pinjam`, `tanggal_pinjam`, `tanggal_kembali`, `Status`, `denda`, `bayar`, `kembali`, `total`, `catatan`, `pengajuan_batas_kembali`, `catatan_pengajuan`, `update_by`, `update_at`, `created_by`, `created_at`) VALUES
(38, 'PJM-260204-000-18', 18, 25, NULL, NULL, NULL, 3, '2026-02-04', '2026-02-11', 'pending', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-02-04 07:36:39', 18, '2026-02-04 07:36:39');

-- --------------------------------------------------------

--
-- Table structure for table `riwayat_peminjaman`
--

CREATE TABLE `riwayat_peminjaman` (
  `riwayat_id` int(11) NOT NULL,
  `peminjaman_id` int(11) NOT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(8) NOT NULL,
  `fullname` varchar(100) NOT NULL,
  `role` enum('admin','user') NOT NULL DEFAULT 'user',
  `status` enum('guru','siswa','lainnya') NOT NULL DEFAULT 'siswa',
  `alamat` varchar(255) DEFAULT NULL,
  `telp` varchar(15) DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `created_by` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `username`, `password`, `fullname`, `role`, `status`, `alamat`, `telp`, `update_by`, `update_at`, `created_by`, `created_at`) VALUES
(12, 'test', '11111111', 'pino', 'admin', 'lainnya', 'test', '085738159689', NULL, NULL, NULL, '2026-01-26 17:00:00'),
(13, 'pino', 'siswa', 'siswa kelas XII', 'user', 'guru', 'Jl. Perpustakaan No. 2', '081234567891', NULL, NULL, NULL, '2026-01-26 17:00:00'),
(14, 'pino', '11111111', 'pino', 'admin', 'siswa', 'anjay', '08987673676', NULL, NULL, NULL, '2026-01-26 17:00:00'),
(15, 'ilham', '12345678', 'ilham raditiya', 'admin', 'guru', 'jl.pendidikan', '086735534566', NULL, NULL, NULL, '2026-01-27 17:00:00'),
(16, 'anjay ', 'alok', 'pino gebastian', 'user', 'siswa', 'test', '0897878372498', NULL, NULL, NULL, '2026-01-27 17:00:00'),
(17, 'ANDA LOGIN SEBAGAIilham', '12345678', 'testingswdgfjsdhlfkhsjih', 'admin', 'guru', 'test', '08983648', 15, '2026-01-28 04:32:26', 14, '2026-01-28 04:14:31'),
(18, 'dimas', '11111111', 'dimas setiyawan', 'user', 'siswa', 'test', '085738159689', NULL, '2026-02-04 07:24:12', NULL, '2026-01-28 05:35:30'),
(19, 'NASRIL ILHAM SAPUTRA', '12345678', '12345678', 'admin', 'lainnya', 'JALAN JALAN', '0808080808089', NULL, '2026-01-28 08:02:34', NULL, '2026-01-28 08:02:34');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `buku`
--
ALTER TABLE `buku`
  ADD PRIMARY KEY (`Buku_id`),
  ADD KEY `kategori_id` (`kategori_id`),
  ADD KEY `update_by` (`update_by`),
  ADD KEY `created_by` (`created_by`);

--
-- Indexes for table `buku_item`
--
ALTER TABLE `buku_item`
  ADD PRIMARY KEY (`bukuitem_id`),
  ADD UNIQUE KEY `kode_buku` (`kode_buku`),
  ADD KEY `buku_id` (`buku_id`);

--
-- Indexes for table `kategori`
--
ALTER TABLE `kategori`
  ADD PRIMARY KEY (`kategori_id`),
  ADD UNIQUE KEY `name_kategori` (`name_kategori`),
  ADD KEY `created_by` (`created_by`),
  ADD KEY `update_by` (`update_by`);

--
-- Indexes for table `peminjaman`
--
ALTER TABLE `peminjaman`
  ADD PRIMARY KEY (`peminjaman_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `buku_id` (`buku_id`),
  ADD KEY `update_by` (`update_by`),
  ADD KEY `created_by` (`created_by`);

--
-- Indexes for table `riwayat_peminjaman`
--
ALTER TABLE `riwayat_peminjaman`
  ADD PRIMARY KEY (`riwayat_id`),
  ADD KEY `peminjaman_id` (`peminjaman_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD KEY `update_by` (`update_by`),
  ADD KEY `created_by` (`created_by`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `buku`
--
ALTER TABLE `buku`
  MODIFY `Buku_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `buku_item`
--
ALTER TABLE `buku_item`
  MODIFY `bukuitem_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `kategori`
--
ALTER TABLE `kategori`
  MODIFY `kategori_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `peminjaman`
--
ALTER TABLE `peminjaman`
  MODIFY `peminjaman_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;

--
-- AUTO_INCREMENT for table `riwayat_peminjaman`
--
ALTER TABLE `riwayat_peminjaman`
  MODIFY `riwayat_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `buku`
--
ALTER TABLE `buku`
  ADD CONSTRAINT `buku_ibfk_1` FOREIGN KEY (`kategori_id`) REFERENCES `kategori` (`kategori_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `buku_ibfk_2` FOREIGN KEY (`update_by`) REFERENCES `user` (`user_id`) ON DELETE SET NULL,
  ADD CONSTRAINT `buku_ibfk_3` FOREIGN KEY (`created_by`) REFERENCES `user` (`user_id`) ON DELETE SET NULL;

--
-- Constraints for table `buku_item`
--
ALTER TABLE `buku_item`
  ADD CONSTRAINT `buku_item_ibfk_1` FOREIGN KEY (`buku_id`) REFERENCES `buku` (`Buku_id`);

--
-- Constraints for table `kategori`
--
ALTER TABLE `kategori`
  ADD CONSTRAINT `kategori_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `user` (`user_id`) ON DELETE SET NULL,
  ADD CONSTRAINT `kategori_ibfk_2` FOREIGN KEY (`update_by`) REFERENCES `user` (`user_id`) ON DELETE SET NULL;

--
-- Constraints for table `peminjaman`
--
ALTER TABLE `peminjaman`
  ADD CONSTRAINT `peminjaman_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `peminjaman_ibfk_2` FOREIGN KEY (`buku_id`) REFERENCES `buku` (`Buku_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `peminjaman_ibfk_3` FOREIGN KEY (`update_by`) REFERENCES `user` (`user_id`) ON DELETE SET NULL,
  ADD CONSTRAINT `peminjaman_ibfk_4` FOREIGN KEY (`created_by`) REFERENCES `user` (`user_id`) ON DELETE SET NULL;

--
-- Constraints for table `riwayat_peminjaman`
--
ALTER TABLE `riwayat_peminjaman`
  ADD CONSTRAINT `riwayat_peminjaman_ibfk_1` FOREIGN KEY (`peminjaman_id`) REFERENCES `peminjaman` (`peminjaman_id`) ON DELETE CASCADE;

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`update_by`) REFERENCES `user` (`user_id`) ON DELETE SET NULL,
  ADD CONSTRAINT `user_ibfk_2` FOREIGN KEY (`created_by`) REFERENCES `user` (`user_id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
