package com.biermacht.brews.utils;

import android.util.Log;

public class ColorHandler {
	
  public static String getSrmColor(double d)
  {
	  
	// Format input
	d = (double) Math.round(d * 100) / 100;
	
	if (d > 40)
		d = 40;
	
	Log.e("ColorHandler", "Received SRM value: " + d);
	
	// Lookup
	if (d == 0.0) {
	return RgbToHex(248,248,248); }
	
	
	if (d == 0.1) {
	return RgbToHex(248,248,230); }


	if (d == 0.2) {
	return RgbToHex(248,248,220); }


	if (d == 0.3) {
	return RgbToHex(247,247,199); }


	if (d == 0.4) {
	return RgbToHex(244,249,185); }


	if (d == 0.5) {
	return RgbToHex(247,249,180); }


	if (d == 0.6) {
	return RgbToHex(248,249,178); }


	if (d == 0.7) {
	return RgbToHex(244,246,169); }


	if (d == 0.8) {
	return RgbToHex(245,247,166); }


	if (d == 0.9) {
	return RgbToHex(246,247,156); }


	if (d == 1) {
	return RgbToHex(243,249,147); }


	if (d == 1.1) {
	return RgbToHex(246,248,141); }


	if (d == 1.2) {
	return RgbToHex(246,249,136); }


	if (d == 1.3) {
	return RgbToHex(245,250,128); }


	if (d == 1.4) {
	return RgbToHex(246,249,121); }


	if (d == 1.5) {
	return RgbToHex(248,249,114); }


	if (d == 1.6) {
	return RgbToHex(243,249,104); }


	if (d == 1.7) {
	return RgbToHex(246,248,107); }


	if (d == 1.8) {
	return RgbToHex(248,247,99); }


	if (d == 1.9) {
	return RgbToHex(245,247,92); }


	if (d == 2) {
	return RgbToHex(248,247,83); }


	if (d == 2.1) {
	return RgbToHex(244,248,72); }


	if (d == 2.2) {
	return RgbToHex(248,247,73); }


	if (d == 2.3) {
	return RgbToHex(246,247,62); }


	if (d == 2.4) {
	return RgbToHex(241,248,53); }


	if (d == 2.5) {
	return RgbToHex(244,247,48); }


	if (d == 2.6) {
	return RgbToHex(246,249,40); }


	if (d == 2.7) {
	return RgbToHex(243,249,34); }


	if (d == 2.8) {
	return RgbToHex(245,247,30); }


	if (d == 2.9) {
	return RgbToHex(248,245,22); }


	if (d == 3) {
	return RgbToHex(246,245,19); }


	if (d == 3.1) {
	return RgbToHex(244,242,22); }


	if (d == 3.2) {
	return RgbToHex(244,240,21); }


	if (d == 3.3) {
	return RgbToHex(243,242,19); }


	if (d == 3.4) {
	return RgbToHex(244,238,24); }


	if (d == 3.5) {
	return RgbToHex(244,237,29); }


	if (d == 3.6) {
	return RgbToHex(238,233,22); }


	if (d == 3.7) {
	return RgbToHex(240,233,23); }


	if (d == 3.8) {
	return RgbToHex(238,231,25); }


	if (d == 3.9) {
	return RgbToHex(234,230,21); }


	if (d == 4) {
	return RgbToHex(236,230,26); }


	if (d == 4.1) {
	return RgbToHex(230,225,24); }


	if (d == 4.2) {
	return RgbToHex(232,225,25); }


	if (d == 4.3) {
	return RgbToHex(230,221,27); }


	if (d == 4.4) {
	return RgbToHex(224,218,23); }


	if (d == 4.5) {
	return RgbToHex(229,216,31); }


	if (d == 4.6) {
	return RgbToHex(229,214,30); }


	if (d == 4.7) {
	return RgbToHex(223,213,26); }


	if (d == 4.8) {
	return RgbToHex(226,213,28); }


	if (d == 4.9) {
	return RgbToHex(223,209,29); }


	if (d == 5) {
	return RgbToHex(224,208,27); }


	if (d == 5.1) {
	return RgbToHex(224,204,32); }


	if (d == 5.2) {
	return RgbToHex(221,204,33); }


	if (d == 5.3) {
	return RgbToHex(220,203,29); }


	if (d == 5.4) {
	return RgbToHex(218,200,32); }


	if (d == 5.5) {
	return RgbToHex(220,197,34); }


	if (d == 5.6) {
	return RgbToHex(218,196,41); }


	if (d == 5.7) {
	return RgbToHex(217,194,43); }


	if (d == 5.8) {
	return RgbToHex(216,192,39); }


	if (d == 5.9) {
	return RgbToHex(213,190,37); }


	if (d == 6) {
	return RgbToHex(213,188,38); }


	if (d == 6.1) {
	return RgbToHex(212,184,39); }


	if (d == 6.2) {
	return RgbToHex(214,183,43); }


	if (d == 6.3) {
	return RgbToHex(213,180,45); }


	if (d == 6.4) {
	return RgbToHex(210,179,41); }


	if (d == 6.5) {
	return RgbToHex(208,178,42); }


	if (d == 6.6) {
	return RgbToHex(208,176,46); }


	if (d == 6.7) {
	return RgbToHex(204,172,48); }


	if (d == 6.8) {
	return RgbToHex(204,172,52); }


	if (d == 6.9) {
	return RgbToHex(205,170,55); }


	if (d == 7) {
	return RgbToHex(201,167,50); }


	if (d == 7.1) {
	return RgbToHex(202,167,52); }


	if (d == 7.2) {
	return RgbToHex(201,166,51); }


	if (d == 7.3) {
	return RgbToHex(199,162,54); }


	if (d == 7.4) {
	return RgbToHex(198,160,56); }


	if (d == 7.5) {
	return RgbToHex(200,158,60); }


	if (d == 7.6) {
	return RgbToHex(194,156,54); }


	if (d == 7.7) {
	return RgbToHex(196,155,54); }


	if (d == 7.8) {
	return RgbToHex(198,151,60); }


	if (d == 7.9) {
	return RgbToHex(193,150,60); }


	if (d == 8) {
	return RgbToHex(191,146,59); }


	if (d == 8.1) {
	return RgbToHex(190,147,57); }


	if (d == 8.2) {
	return RgbToHex(190,147,59); }


	if (d == 8.3) {
	return RgbToHex(190,145,60); }


	if (d == 8.4) {
	return RgbToHex(186,148,56); }


	if (d == 8.5) {
	return RgbToHex(190,145,58); }


	if (d == 8.6) {
	return RgbToHex(193,145,59); }


	if (d == 8.7) {
	return RgbToHex(190,145,58); }


	if (d == 8.8) {
	return RgbToHex(191,143,59); }


	if (d == 8.9) {
	return RgbToHex(191,141,61); }


	if (d == 9) {
	return RgbToHex(190,140,58); }


	if (d == 9.1) {
	return RgbToHex(192,140,61); }


	if (d == 9.2) {
	return RgbToHex(193,138,62); }


	if (d == 9.3) {
	return RgbToHex(192,137,59); }


	if (d == 9.4) {
	return RgbToHex(193,136,59); }


	if (d == 9.5) {
	return RgbToHex(195,135,63); }


	if (d == 9.6) {
	return RgbToHex(191,136,58); }


	if (d == 9.7) {
	return RgbToHex(191,134,67); }


	if (d == 9.8) {
	return RgbToHex(193,131,67); }


	if (d == 9.9) {
	return RgbToHex(190,130,58); }


	if (d == 10) {
	return RgbToHex(191,129,58); }


	if (d == 10.1) {
	return RgbToHex(191,131,57); }


	if (d == 10.2) {
	return RgbToHex(191,129,58); }


	if (d == 10.3) {
	return RgbToHex(191,129,58); }


	if (d == 10.4) {
	return RgbToHex(190,129,55); }


	if (d == 10.5) {
	return RgbToHex(191,127,59); }


	if (d == 10.6) {
	return RgbToHex(194,126,59); }


	if (d == 10.7) {
	return RgbToHex(188,128,54); }


	if (d == 10.8) {
	return RgbToHex(190,124,55); }


	if (d == 10.9) {
	return RgbToHex(193,122,55); }


	if (d == 11) {
	return RgbToHex(190,124,55); }


	if (d == 11.1) {
	return RgbToHex(194,121,59); }


	if (d == 11.2) {
	return RgbToHex(193,120,56); }


	if (d == 11.3) {
	return RgbToHex(190,119,52); }


	if (d == 11.4) {
	return RgbToHex(182,117,54); }


	if (d == 11.5) {
	return RgbToHex(196,116,59); }


	if (d == 11.6) {
	return RgbToHex(191,118,56); }


	if (d == 11.7) {
	return RgbToHex(190,116,57); }


	if (d == 11.8) {
	return RgbToHex(191,115,58); }


	if (d == 11.9) {
	return RgbToHex(189,115,56); }


	if (d == 12) {
	return RgbToHex(191,113,56); }


	if (d == 12.1) {
	return RgbToHex(191,113,53); }


	if (d == 12.2) {
	return RgbToHex(188,112,57); }


	if (d == 12.3) {
	return RgbToHex(190,112,55); }


	if (d == 12.4) {
	return RgbToHex(184,110,52); }


	if (d == 12.5) {
	return RgbToHex(188,109,55); }


	if (d == 12.6) {
	return RgbToHex(189,109,55); }


	if (d == 12.7) {
	return RgbToHex(186,106,50); }


	if (d == 12.8) {
	return RgbToHex(190,103,52); }


	if (d == 12.9) {
	return RgbToHex(189,104,54); }


	if (d == 13) {
	return RgbToHex(188,103,51); }


	if (d == 13.1) {
	return RgbToHex(188,103,51); }


	if (d == 13.2) {
	return RgbToHex(186,101,51); }


	if (d == 13.3) {
	return RgbToHex(186,102,56); }


	if (d == 13.4) {
	return RgbToHex(185,100,56); }


	if (d == 13.5) {
	return RgbToHex(185,98,59); }


	if (d == 13.6) {
	return RgbToHex(183,98,54); }


	if (d == 13.7) {
	return RgbToHex(181,100,53); }


	if (d == 13.8) {
	return RgbToHex(182,97,55); }


	if (d == 13.9) {
	return RgbToHex(177,97,51); }


	if (d == 14) {
	return RgbToHex(178,96,51); }


	if (d == 14.1) {
	return RgbToHex(176,96,49); }


	if (d == 14.2) {
	return RgbToHex(177,96,55); }


	if (d == 14.3) {
	return RgbToHex(178,95,55); }


	if (d == 14.4) {
	return RgbToHex(171,94,55); }


	if (d == 14.5) {
	return RgbToHex(171,92,56); }


	if (d == 14.6) {
	return RgbToHex(172,93,59); }


	if (d == 14.7) {
	return RgbToHex(168,92,55); }


	if (d == 14.8) {
	return RgbToHex(169,90,54); }


	if (d == 14.9) {
	return RgbToHex(168,88,57); }


	if (d == 15) {
	return RgbToHex(165,89,54); }


	if (d == 15.1) {
	return RgbToHex(166,88,54); }


	if (d == 15.2) {
	return RgbToHex(165,88,58); }


	if (d == 15.3) {
	return RgbToHex(161,88,52); }


	if (d == 15.4) {
	return RgbToHex(163,85,55); }


	if (d == 15.5) {
	return RgbToHex(160,86,56); }


	if (d == 15.6) {
	return RgbToHex(158,85,57); }


	if (d == 15.7) {
	return RgbToHex(158,86,54); }


	if (d == 15.8) {
	return RgbToHex(159,84,57); }


	if (d == 15.9) {
	return RgbToHex(156,83,53); }


	if (d == 16) {
	return RgbToHex(152,83,54); }


	if (d == 16.1) {
	return RgbToHex(150,83,55); }


	if (d == 16.2) {
	return RgbToHex(150,81,56); }


	if (d == 16.3) {
	return RgbToHex(146,81,56); }


	if (d == 16.4) {
	return RgbToHex(147,79,54); }


	if (d == 16.5) {
	return RgbToHex(147,79,55); }


	if (d == 16.6) {
	return RgbToHex(146,78,54); }


	if (d == 16.7) {
	return RgbToHex(142,77,51); }


	if (d == 16.8) {
	return RgbToHex(143,79,53); }


	if (d == 16.9) {
	return RgbToHex(142,77,54); }


	if (d == 17) {
	return RgbToHex(141,76,50); }


	if (d == 17.1) {
	return RgbToHex(140,75,50); }


	if (d == 17.2) {
	return RgbToHex(138,73,49); }


	if (d == 17.3) {
	return RgbToHex(135,70,45); }


	if (d == 17.4) {
	return RgbToHex(136,71,49); }


	if (d == 17.5) {
	return RgbToHex(140,72,49); }


	if (d == 17.6) {
	return RgbToHex(128,70,45); }


	if (d == 17.7) {
	return RgbToHex(129,71,46); }


	if (d == 17.8) {
	return RgbToHex(130,69,47); }


	if (d == 17.9) {
	return RgbToHex(123,69,45); }


	if (d == 18) {
	return RgbToHex(124,69,45); }


	if (d == 18.1) {
	return RgbToHex(121,66,40); }


	if (d == 18.2) {
	return RgbToHex(120,67,40); }


	if (d == 18.3) {
	return RgbToHex(119,64,38); }


	if (d == 18.4) {
	return RgbToHex(116,63,34); }


	if (d == 18.5) {
	return RgbToHex(120,63,35); }


	if (d == 18.6) {
	return RgbToHex(120,62,37); }


	if (d == 18.7) {
	return RgbToHex(112,63,35); }


	if (d == 18.8) {
	return RgbToHex(111,62,36); }


	if (d == 18.9) {
	return RgbToHex(109,60,34); }


	if (d == 19) {
	return RgbToHex(107,58,30); }


	if (d == 19.1) {
	return RgbToHex(106,57,31); }


	if (d == 19.2) {
	return RgbToHex(107,56,31); }


	if (d == 19.3) {
	return RgbToHex(105,56,28); }


	if (d == 19.4) {
	return RgbToHex(105,56,28); }


	if (d == 19.5) {
	return RgbToHex(104,52,31); }


	if (d == 19.6) {
	return RgbToHex(102,53,27); }


	if (d == 19.7) {
	return RgbToHex(100,53,26); }


	if (d == 19.8) {
	return RgbToHex(99,52,25); }


	if (d == 19.9) {
	return RgbToHex(93,53,24); }


	if (d == 20) {
	return RgbToHex(93,52,26); }


	if (d == 20.1) {
	return RgbToHex(89,49,20); }


	if (d == 20.2) {
	return RgbToHex(90,50,21); }


	if (d == 20.3) {
	return RgbToHex(91,48,20); }


	if (d == 20.4) {
	return RgbToHex(83,48,15); }


	if (d == 20.5) {
	return RgbToHex(88,48,17); }


	if (d == 20.6) {
	return RgbToHex(86,46,17); }


	if (d == 20.7) {
	return RgbToHex(81,45,15); }


	if (d == 20.8) {
	return RgbToHex(83,44,15); }


	if (d == 20.9) {
	return RgbToHex(81,45,15); }


	if (d == 21) {
	return RgbToHex(78,42,12); }


	if (d == 21.1) {
	return RgbToHex(77,43,12); }


	if (d == 21.2) {
	return RgbToHex(75,41,12); }


	if (d == 21.3) {
	return RgbToHex(74,41,5); }


	if (d == 21.4) {
	return RgbToHex(78,40,23); }


	if (d == 21.5) {
	return RgbToHex(83,43,46); }


	if (d == 21.6) {
	return RgbToHex(78,43,41); }


	if (d == 21.7) {
	return RgbToHex(78,40,41); }


	if (d == 21.8) {
	return RgbToHex(76,41,41); }


	if (d == 21.9) {
	return RgbToHex(74,39,39); }


	if (d == 22) {
	return RgbToHex(74,39,39); }


	if (d == 22.1) {
	return RgbToHex(69,39,35); }


	if (d == 22.2) {
	return RgbToHex(70,37,37); }


	if (d == 22.3) {
	return RgbToHex(68,38,36); }


	if (d == 22.4) {
	return RgbToHex(64,35,34); }


	if (d == 22.5) {
	return RgbToHex(64,35,34); }


	if (d == 22.6) {
	return RgbToHex(62,33,32); }


	if (d == 22.7) {
	return RgbToHex(58,33,31); }


	if (d == 22.8) {
	return RgbToHex(61,33,31); }


	if (d == 22.9) {
	return RgbToHex(58,33,33); }


	if (d == 23) {
	return RgbToHex(54,31,27); }


	if (d == 23.1) {
	return RgbToHex(52,29,28); }


	if (d == 23.2) {
	return RgbToHex(52,29,28); }


	if (d == 23.3) {
	return RgbToHex(49,28,27); }


	if (d == 23.4) {
	return RgbToHex(48,27,26); }


	if (d == 23.5) {
	return RgbToHex(48,27,26); }


	if (d == 23.6) {
	return RgbToHex(44,25,25); }


	if (d == 23.7) {
	return RgbToHex(44,25,23); }


	if (d == 23.8) {
	return RgbToHex(42,24,26); }


	if (d == 23.9) {
	return RgbToHex(40,23,22); }


	if (d == 24) {
	return RgbToHex(38,23,22); }


	if (d == 24.1) {
	return RgbToHex(38,23,22); }


	if (d == 24.2) {
	return RgbToHex(38,23,22); }


	if (d == 24.3) {
	return RgbToHex(38,23,22); }


	if (d == 24.4) {
	return RgbToHex(38,23,22); }


	if (d == 24.5) {
	return RgbToHex(38,23,22); }


	if (d == 24.6) {
	return RgbToHex(38,23,22); }


	if (d == 24.7) {
	return RgbToHex(38,23,22); }


	if (d == 24.8) {
	return RgbToHex(38,23,22); }


	if (d == 24.9) {
	return RgbToHex(38,23,22); }


	if (d == 25) {
	return RgbToHex(38,23,22); }


	if (d == 25.1) {
	return RgbToHex(38,23,22); }


	if (d == 25.2) {
	return RgbToHex(38,23,22); }


	if (d == 25.3) {
	return RgbToHex(38,23,22); }


	if (d == 25.4) {
	return RgbToHex(38,23,22); }


	if (d == 25.5) {
	return RgbToHex(38,23,22); }


	if (d == 25.6) {
	return RgbToHex(38,23,24); }


	if (d == 25.7) {
	return RgbToHex(25,16,15); }


	if (d == 25.8) {
	return RgbToHex(25,16,15); }


	if (d == 25.9) {
	return RgbToHex(25,16,15); }


	if (d == 26) {
	return RgbToHex(25,16,15); }


	if (d == 26.1) {
	return RgbToHex(25,16,15); }


	if (d == 26.2) {
	return RgbToHex(25,16,15); }


	if (d == 26.3) {
	return RgbToHex(25,16,15); }


	if (d == 26.4) {
	return RgbToHex(25,16,15); }


	if (d == 26.5) {
	return RgbToHex(25,16,15); }


	if (d == 26.6) {
	return RgbToHex(25,16,15); }


	if (d == 26.7) {
	return RgbToHex(25,16,15); }


	if (d == 26.8) {
	return RgbToHex(25,16,15); }


	if (d == 26.9) {
	return RgbToHex(25,16,15); }


	if (d == 27) {
	return RgbToHex(25,16,15); }


	if (d == 27.1) {
	return RgbToHex(25,16,15); }


	if (d == 27.2) {
	return RgbToHex(25,16,15); }


	if (d == 27.3) {
	return RgbToHex(18,13,12); }


	if (d == 27.4) {
	return RgbToHex(18,13,12); }


	if (d == 27.5) {
	return RgbToHex(18,13,12); }


	if (d == 27.6) {
	return RgbToHex(18,13,12); }


	if (d == 27.7) {
	return RgbToHex(18,13,12); }


	if (d == 27.8) {
	return RgbToHex(18,13,12); }


	if (d == 27.9) {
	return RgbToHex(18,13,12); }


	if (d == 28) {
	return RgbToHex(18,13,12); }


	if (d == 28.1) {
	return RgbToHex(18,13,12); }


	if (d == 28.2) {
	return RgbToHex(18,13,12); }


	if (d == 28.3) {
	return RgbToHex(18,13,12); }


	if (d == 28.4) {
	return RgbToHex(18,13,12); }


	if (d == 28.5) {
	return RgbToHex(18,13,12); }


	if (d == 28.6) {
	return RgbToHex(18,13,12); }


	if (d == 28.7) {
	return RgbToHex(17,13,10); }


	if (d == 28.8) {
	return RgbToHex(18,13,12); }


	if (d == 28.9) {
	return RgbToHex(16,11,10); }


	if (d == 29) {
	return RgbToHex(16,11,10); }


	if (d == 29.1) {
	return RgbToHex(16,11,10); }


	if (d == 29.2) {
	return RgbToHex(16,11,10); }


	if (d == 29.3) {
	return RgbToHex(16,11,10); }


	if (d == 29.4) {
	return RgbToHex(16,11,10); }


	if (d == 29.5) {
	return RgbToHex(16,11,10); }


	if (d == 29.6) {
	return RgbToHex(16,11,10); }


	if (d == 29.7) {
	return RgbToHex(16,11,10); }


	if (d == 29.8) {
	return RgbToHex(16,11,10); }


	if (d == 29.9) {
	return RgbToHex(16,11,10); }


	if (d == 30) {
	return RgbToHex(16,11,10); }


	if (d == 30.1) {
	return RgbToHex(16,11,10); }


	if (d == 30.2) {
	return RgbToHex(16,11,10); }


	if (d == 30.3) {
	return RgbToHex(16,11,10); }


	if (d == 30.4) {
	return RgbToHex(16,11,10); }


	if (d == 30.5) {
	return RgbToHex(14,9,8); }


	if (d == 30.6) {
	return RgbToHex(15,10,9); }


	if (d == 30.7) {
	return RgbToHex(14,9,8); }


	if (d == 30.8) {
	return RgbToHex(14,9,8); }


	if (d == 30.9) {
	return RgbToHex(14,9,8); }


	if (d == 31) {
	return RgbToHex(14,9,8); }


	if (d == 31.1) {
	return RgbToHex(14,9,8); }


	if (d == 31.2) {
	return RgbToHex(14,9,8); }


	if (d == 31.3) {
	return RgbToHex(14,9,8); }


	if (d == 31.4) {
	return RgbToHex(14,9,8); }


	if (d == 31.5) {
	return RgbToHex(14,9,8); }


	if (d == 31.6) {
	return RgbToHex(14,9,8); }


	if (d == 31.7) {
	return RgbToHex(14,9,8); }


	if (d == 31.8) {
	return RgbToHex(14,9,8); }


	if (d == 31.9) {
	return RgbToHex(14,9,8); }


	if (d == 32) {
	return RgbToHex(15,11,8); }


	if (d == 32.1) {
	return RgbToHex(12,9,7); }


	if (d == 32.2) {
	return RgbToHex(12,9,7); }


	if (d == 32.3) {
	return RgbToHex(12,9,7); }


	if (d == 32.4) {
	return RgbToHex(12,9,7); }


	if (d == 32.5) {
	return RgbToHex(12,9,7); }


	if (d == 32.6) {
	return RgbToHex(12,9,7); }


	if (d == 32.7) {
	return RgbToHex(12,9,7); }


	if (d == 32.8) {
	return RgbToHex(12,9,7); }


	if (d == 32.9) {
	return RgbToHex(12,9,7); }


	if (d == 33) {
	return RgbToHex(12,9,7); }


	if (d == 33.1) {
	return RgbToHex(12,9,7); }


	if (d == 33.2) {
	return RgbToHex(12,9,7); }


	if (d == 33.3) {
	return RgbToHex(12,9,7); }


	if (d == 33.4) {
	return RgbToHex(12,9,7); }


	if (d == 33.5) {
	return RgbToHex(12,9,7); }


	if (d == 33.6) {
	return RgbToHex(12,9,7); }


	if (d == 33.7) {
	return RgbToHex(10,7,7); }


	if (d == 33.8) {
	return RgbToHex(10,7,5); }


	if (d == 33.9) {
	return RgbToHex(8,7,7); }


	if (d == 34) {
	return RgbToHex(8,7,7); }


	if (d == 34.1) {
	return RgbToHex(8,7,7); }


	if (d == 34.2) {
	return RgbToHex(8,7,7); }


	if (d == 34.3) {
	return RgbToHex(8,7,7); }


	if (d == 34.4) {
	return RgbToHex(8,7,7); }


	if (d == 34.5) {
	return RgbToHex(8,7,7); }


	if (d == 34.6) {
	return RgbToHex(8,7,7); }


	if (d == 34.7) {
	return RgbToHex(8,7,7); }


	if (d == 34.8) {
	return RgbToHex(8,7,7); }


	if (d == 34.9) {
	return RgbToHex(8,7,7); }


	if (d == 35) {
	return RgbToHex(8,7,7); }


	if (d == 35.1) {
	return RgbToHex(8,7,7); }


	if (d == 35.2) {
	return RgbToHex(8,7,7); }


	if (d == 35.3) {
	return RgbToHex(7,6,6); }


	if (d == 35.4) {
	return RgbToHex(7,6,6); }


	if (d == 35.5) {
	return RgbToHex(7,6,6); }


	if (d == 35.6) {
	return RgbToHex(7,6,6); }


	if (d == 35.7) {
	return RgbToHex(7,6,6); }


	if (d == 35.8) {
	return RgbToHex(7,6,6); }


	if (d == 35.9) {
	return RgbToHex(7,6,6); }


	if (d == 36) {
	return RgbToHex(7,6,6); }


	if (d == 36.1) {
	return RgbToHex(7,6,6); }


	if (d == 36.2) {
	return RgbToHex(7,6,6); }


	if (d == 36.3) {
	return RgbToHex(7,6,6); }


	if (d == 36.4) {
	return RgbToHex(7,6,6); }


	if (d == 36.5) {
	return RgbToHex(7,6,6); }


	if (d == 36.6) {
	return RgbToHex(7,6,6); }


	if (d == 36.7) {
	return RgbToHex(7,7,4); }


	if (d == 36.8) {
	return RgbToHex(6,6,3); }


	if (d == 36.9) {
	return RgbToHex(6,5,5); }


	if (d == 37) {
	return RgbToHex(4,5,4); }


	if (d == 37.1) {
	return RgbToHex(4,5,4); }


	if (d == 37.2) {
	return RgbToHex(4,5,4); }


	if (d == 37.3) {
	return RgbToHex(4,5,4); }


	if (d == 37.4) {
	return RgbToHex(4,5,4); }


	if (d == 37.5) {
	return RgbToHex(4,5,4); }


	if (d == 37.6) {
	return RgbToHex(4,5,4); }


	if (d == 37.7) {
	return RgbToHex(4,5,4); }


	if (d == 37.8) {
	return RgbToHex(4,5,4); }


	if (d == 37.9) {
	return RgbToHex(4,5,4); }


	if (d == 38) {
	return RgbToHex(4,5,4); }


	if (d == 38.1) {
	return RgbToHex(4,5,4); }


	if (d == 38.2) {
	return RgbToHex(4,5,4); }


	if (d == 38.3) {
	return RgbToHex(4,5,4); }


	if (d == 38.4) {
	return RgbToHex(4,5,4); }


	if (d == 38.5) {
	return RgbToHex(3,4,3); }


	if (d == 38.6) {
	return RgbToHex(4,5,4); }


	if (d == 38.7) {
	return RgbToHex(3,4,3); }


	if (d == 38.8) {
	return RgbToHex(3,4,3); }


	if (d == 38.9) {
	return RgbToHex(3,4,3); }


	if (d == 39) {
	return RgbToHex(3,4,3); }


	if (d == 39.1) {
	return RgbToHex(3,4,3); }


	if (d == 39.2) {
	return RgbToHex(3,4,3); }


	if (d == 39.3) {
	return RgbToHex(3,4,3); }


	if (d == 39.4) {
	return RgbToHex(3,4,3); }


	if (d == 39.5) {
	return RgbToHex(3,4,3); }


	if (d == 39.6) {
	return RgbToHex(3,4,3); }


	if (d == 39.7) {
	return RgbToHex(3,4,3); }


	if (d == 39.8) {
	return RgbToHex(3,4,3); }


	if (d == 39.9) {
	return RgbToHex(3,4,3); }


	if (d == 40) {
	return RgbToHex(3,4,3); }
	
	else
	{
		Log.e("COLOR", "Could not locate color.");
		return RgbToHex(0,0,0);
	}
  }

  private static String RgbToHex(Integer R, Integer G, Integer B) {

	String hexColor = "#FFFFFF";
	
	String rString = Integer.toHexString(R);
	String gString = Integer.toHexString(G);
	String bString = Integer.toHexString(B);
	
	if (rString.length() < 2)
		rString = "0" + rString;
	if (gString.length() < 2)
		gString = "0" + gString;
	if (bString.length() < 2)
		bString = "0" + bString;
	
	hexColor = "#" + rString + gString + bString;
	Log.e("ColorHandler", "Produced hex color: " + R + G + B + " : " + hexColor);
	
 	
  return hexColor;
  }

}
