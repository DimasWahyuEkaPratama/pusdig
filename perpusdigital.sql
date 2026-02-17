-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 12, 2026 at 08:24 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

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
  `stok` int(225) NOT NULL DEFAULT 0,
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
(25, 'NULL', 'NULL', 'NULL', '2026-02-02', 0, 4, 'NULL', 'C:\\Users\\ASUS vivobook\\OneDrive\\ドキュメント\\Aston.jpg', 'NULL', 14, '2026-02-12 01:40:17', NULL, '2026-02-02 03:59:28'),
(47, 'GEOGRAFI', 'Pino', 'alif', '2026-02-09', 80, 8, 'RAK No.3', 'C:\\Users\\ASUS vivobook\\OneDrive\\ドキュメント\\Bu Ika dimas.png', 'test', 14, '2026-02-12 04:54:11', 14, '2026-02-09 14:18:34'),
(48, 'CONTOH', 'COTOH', 'COTOH', '2026-02-11', 98, 15, 'RAK 3', 'C:\\Users\\ASUS vivobook\\OneDrive\\ドキュメント\\Hummatech.jpg', 'KJGHKL', 14, '2026-02-12 03:35:27', 14, '2026-02-11 03:28:56'),
(49, 'COBA TEST', 'COBA', 'COBA', '2026-02-11', 1, 1, 'COBA', 'C:\\Users\\ASUS vivobook\\OneDrive\\ドキュメント\\Laskar Buah.jpg', 'COBA', 14, '2026-02-12 02:29:20', 14, '2026-02-11 14:13:43'),
(50, 'KIK', 'pino', 'pino', '2026-02-12', 5, 17, 'Rak No.12', 'C:\\Users\\ASUS vivobook\\OneDrive\\ドキュメント\\tenda-java.png', 'lorem ipdsum', 14, '2026-02-12 02:12:59', 14, '2026-02-12 02:12:59');

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
(14, 25, 'N/A', '', '2026-02-01 17:00:00', '2026-02-04 06:40:54'),
(23, 47, 'BK-0004', 'tersedia', '2026-02-09 19:40:13', '2026-02-09 19:40:13'),
(24, 47, 'BK-0005', 'tersedia', '2026-02-09 19:40:13', '2026-02-11 14:30:32'),
(25, 47, 'BK-0006', 'rusak', '2026-02-09 19:40:13', '2026-02-11 14:34:45'),
(26, 47, 'BK-0007', 'tersedia', '2026-02-09 19:40:13', '2026-02-09 19:40:13'),
(27, 48, 'BK-0008', 'tersedia', '2026-02-10 17:00:00', '2026-02-11 03:28:56'),
(28, 48, 'BK-0009', 'tersedia', '2026-02-10 17:00:00', '2026-02-11 03:28:56'),
(29, 48, 'BK-0010', 'tersedia', '2026-02-10 17:00:00', '2026-02-11 03:28:56'),
(30, 48, 'BK-0011', 'tersedia', '2026-02-10 17:00:00', '2026-02-11 03:28:56'),
(31, 49, 'BK-0012', 'tersedia', '2026-02-10 17:00:00', '2026-02-11 14:13:43'),
(32, 49, 'BK-0013', 'tersedia', '2026-02-10 17:00:00', '2026-02-11 14:13:43'),
(33, 50, 'BK-0014', 'tersedia', '2026-02-11 17:00:00', '2026-02-12 02:12:59'),
(34, 50, 'BK-0015', 'tersedia', '2026-02-11 17:00:00', '2026-02-12 02:12:59'),
(35, 50, 'BK-0016', 'tersedia', '2026-02-11 17:00:00', '2026-02-12 02:12:59'),
(36, 50, 'BK-0017', 'tersedia', '2026-02-11 17:00:00', '2026-02-12 02:12:59'),
(37, 50, 'BK-0018', 'tersedia', '2026-02-11 17:00:00', '2026-02-12 02:12:59');

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
(14, 'CERITA RAKYAT', '2026-02-09 04:22:15', 14, '2026-02-09 04:22:52', 14),
(15, 'CONTOH', '2026-02-11 03:27:25', 14, '2026-02-11 03:27:25', 14),
(17, 'KIK', '2026-02-12 02:11:56', 14, '2026-02-12 02:11:56', 14);

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
  `tanggal_pinjam` date NOT NULL,
  `tanggal_kembali` date DEFAULT NULL,
  `Status` enum('pending','dipinjam','selesai','ditolak','diterima','diperpanjang') NOT NULL DEFAULT 'pending',
  `denda` int(11) DEFAULT 0,
  `bayar` int(255) DEFAULT NULL,
  `kembali` int(255) DEFAULT NULL,
  `total` int(255) DEFAULT NULL,
  `catatan` varchar(255) DEFAULT NULL,
  `catatan_pengajuan` varchar(225) DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `created_by` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `peminjaman`
--

INSERT INTO `peminjaman` (`peminjaman_id`, `kode_peminjaman`, `user_id`, `buku_id`, `kd_bk1`, `kd_bk2`, `kd_bk3`, `jumlah_pinjam`, `tanggal_pinjam`, `tanggal_kembali`, `Status`, `denda`, `bayar`, `kembali`, `total`, `catatan`, `catatan_pengajuan`, `update_by`, `update_at`, `created_by`, `created_at`) VALUES
(55, 'PJM-260212-001-25', 25, 47, 'BK-0005', 'N/A', 'N/A', 1, '2026-02-12', '2026-02-19', 'dipinjam', 0, NULL, NULL, NULL, '', NULL, 14, '2026-02-12 02:37:49', 25, '2026-02-12 02:29:01'),
(57, 'PJM-260212-003-25', 25, 49, 'BK-0008', 'N/A', 'N/A', 1, '2026-02-12', '2026-02-19', 'dipinjam', 0, NULL, NULL, NULL, 'oke', NULL, 14, '2026-02-12 02:45:22', 25, '2026-02-12 02:29:20'),
(58, 'PJM-260212-004-22', 22, 48, NULL, NULL, NULL, 1, '2026-02-12', '2026-02-19', 'pending', 0, NULL, NULL, NULL, NULL, NULL, NULL, '2026-02-12 03:35:27', 22, '2026-02-12 03:35:27'),
(59, 'PJM-260212-005-22', 22, 47, NULL, NULL, NULL, 2, '2026-02-12', '2026-02-19', 'pending', 0, NULL, NULL, NULL, NULL, NULL, NULL, '2026-02-12 03:35:34', 22, '2026-02-12 03:35:34'),
(60, 'PJM-260212-006-23', 23, 47, NULL, NULL, NULL, 1, '2026-02-12', '2026-02-19', 'pending', 0, NULL, NULL, NULL, NULL, NULL, NULL, '2026-02-12 04:53:59', 23, '2026-02-12 04:53:59'),
(61, 'PJM-260212-007-23', 23, 47, NULL, NULL, NULL, 2, '2026-02-12', '2026-02-19', 'pending', 0, NULL, NULL, NULL, NULL, NULL, NULL, '2026-02-12 04:54:11', 23, '2026-02-12 04:54:11');

-- --------------------------------------------------------

--
-- Table structure for table `riwayat_peminjaman`
--

CREATE TABLE `riwayat_peminjaman` (
  `riwayat_id` int(11) NOT NULL,
  `peminjaman_id` varchar(20) NOT NULL,
  `kode_peminjaman` varchar(20) NOT NULL,
  `user_id` int(11) NOT NULL,
  `buku_id` int(11) NOT NULL,
  `kd_bk1` varchar(9) DEFAULT NULL,
  `kd_bk2` varchar(9) DEFAULT NULL,
  `kd_bk3` varchar(9) DEFAULT NULL,
  `jumlah_pinjam` int(11) NOT NULL DEFAULT 1,
  `tanggal_pinjam` date NOT NULL,
  `tanggal_kembali` date DEFAULT NULL,
  `Status` enum('pending','dipinjam','selesai','ditolak','diterima','diperpanjang') NOT NULL DEFAULT 'pending',
  `denda` int(11) DEFAULT 0,
  `bayar` int(11) DEFAULT NULL,
  `kembali` int(11) DEFAULT NULL,
  `total` int(11) DEFAULT NULL,
  `catatan` varchar(255) DEFAULT NULL,
  `catatan_pengajuan` varchar(255) DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  `update_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `created_by` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `riwayat_peminjaman`
--

INSERT INTO `riwayat_peminjaman` (`riwayat_id`, `peminjaman_id`, `kode_peminjaman`, `user_id`, `buku_id`, `kd_bk1`, `kd_bk2`, `kd_bk3`, `jumlah_pinjam`, `tanggal_pinjam`, `tanggal_kembali`, `Status`, `denda`, `bayar`, `kembali`, `total`, `catatan`, `catatan_pengajuan`, `update_by`, `update_at`, `created_by`, `created_at`) VALUES
(5, '44', 'PJM-260210-000-18', 18, 47, NULL, NULL, NULL, 1, '2026-02-10', '2026-02-17', 'selesai', 0, NULL, NULL, NULL, NULL, NULL, NULL, '2026-02-10 00:56:37', 18, '2026-02-09 22:21:45'),
(6, '51', 'PJM-260211-003-18', 18, 47, 'BK-0004', 'BK-0008', 'N/A', 2, '2026-02-11', '2026-02-12', 'selesai', 0, 0, 0, 0, 'belum selesai membaca', 'belum selesai membaca', 14, '2026-02-11 17:14:47', 18, '2026-02-11 13:09:08'),
(7, '53', 'PJM-260212-001-18', 18, 47, 'BK-0004', 'BK-0005', 'BK-0006', 3, '2026-02-02', '2026-02-11', 'selesai', 1000, 2000, 1000, 1000, '', 'belum selesai dibaca', 14, '2026-02-12 02:19:11', 18, '2026-02-12 02:10:19'),
(8, '56', 'PJM-260212-002-25', 25, 48, 'BK-0008', 'N/A', 'N/A', 1, '2026-02-12', '2026-02-24', 'selesai', 0, 0, 0, 0, 'OKE', 'wetrtyt', 14, '2026-02-12 03:38:03', 25, '2026-02-12 02:29:11');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `nomor` varchar(255) NOT NULL,
  `password` varchar(8) NOT NULL,
  `fullname` varchar(100) NOT NULL,
  `role` enum('admin','user') NOT NULL DEFAULT 'user',
  `status` enum('guru','siswa','pengunjung') NOT NULL DEFAULT 'siswa',
  `alamat` varchar(255) DEFAULT NULL,
  `telp` varchar(15) DEFAULT NULL,
  `email` varchar(225) DEFAULT NULL,
  `update_by` int(11) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `created_by` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `nomor`, `password`, `fullname`, `role`, `status`, `alamat`, `telp`, `email`, `update_by`, `update_at`, `created_by`, `created_at`) VALUES
(12, 'test', '11111111', 'pino', 'user', 'pengunjung', 'test', '085738159689', NULL, NULL, NULL, NULL, '2026-01-26 17:00:00'),
(14, 'pino', '11111111', 'pino', 'admin', 'guru', 'ghgjkkhjkjdfg', '08987673676', 'idpin@gmail.com', 14, '2026-02-11 13:58:17', NULL, '2026-01-26 17:00:00'),
(15, 'ilham', '12345678', 'ilham raditiya', 'admin', 'guru', 'jl.pendidikan', '086735534566', NULL, NULL, NULL, NULL, '2026-01-27 17:00:00'),
(16, 'anjay ', 'alok', 'pino gebastian', 'user', 'siswa', 'test', '0897878372498', NULL, NULL, NULL, NULL, '2026-01-27 17:00:00'),
(17, 'ANDA LOGIN SEBAGAIilham', '12345678', 'testingswdgfjsdhlfkhsjih', 'admin', 'guru', 'test', '08983648', NULL, 15, '2026-01-28 04:32:26', 14, '2026-01-28 04:14:31'),
(18, 'dimas', '11111111', 'DIMAS ANJAY MABAR', 'user', 'siswa', 'test', '085738159689', NULL, 14, '2026-02-09 04:31:52', NULL, '2026-01-28 05:35:30'),
(20, 'alif', '11111111', 'alif syafiudin', 'user', '', 'Jl. raya bakalan ', '0000000000000', NULL, 14, '2026-02-09 04:28:29', 14, '2026-02-09 04:25:28'),
(22, 'pinotest', '1111111', 'alifvino', 'user', 'guru', 'wadsf', 'pino@gmail.com', '11111111', NULL, '2026-02-11 11:04:25', NULL, '2026-02-11 11:04:25'),
(23, '12345678', '87654321', 'ilham', 'user', 'siswa', 'jl.pustaka', '081250764329', 'ilhamraditiya@gmail.com', NULL, '2026-02-12 01:15:01', NULL, '2026-02-12 01:15:01'),
(24, '87654321', '12345678', 'pino', 'admin', 'guru', 'jl.pendidikan', '096243216547', 'pino@gmail.com', NULL, '2026-02-12 01:17:24', NULL, '2026-02-12 01:17:24'),
(25, '0089131', '11111111', 'Muhammd Rizki Alifvino', 'user', 'siswa', 'bakalan', '085738159689', 'pino@gmail.com', NULL, '2026-02-12 02:24:26', NULL, '2026-02-12 02:24:26');

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
  ADD PRIMARY KEY (`riwayat_id`);

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
  MODIFY `Buku_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

--
-- AUTO_INCREMENT for table `buku_item`
--
ALTER TABLE `buku_item`
  MODIFY `bukuitem_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;

--
-- AUTO_INCREMENT for table `kategori`
--
ALTER TABLE `kategori`
  MODIFY `kategori_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `peminjaman`
--
ALTER TABLE `peminjaman`
  MODIFY `peminjaman_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=62;

--
-- AUTO_INCREMENT for table `riwayat_peminjaman`
--
ALTER TABLE `riwayat_peminjaman`
  MODIFY `riwayat_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

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
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`update_by`) REFERENCES `user` (`user_id`) ON DELETE SET NULL,
  ADD CONSTRAINT `user_ibfk_2` FOREIGN KEY (`created_by`) REFERENCES `user` (`user_id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
