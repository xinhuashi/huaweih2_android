package com.palmap.demo.huaweih2.model;

/**
 * Created by eric3 on 2016/10/23.
 */

public class RoutePoi {
//  public static long[] pois = new long[]{1263114,1263110,1284127,1264693,1263244,1263263,1263262,1263249,1263245,1263251,1263261,1263260,1263247,1263259,1263255,1263248,1263250,1263258,1263257,1263252,1263264,1263254,1263246,1263253,1263256,1264651,1264650,1262978,1262977,1262959,1262964,1262960,1262976,1262975,1262966,1262974,1262962,1262965,1262973,1262970,1262963,1262967,1262972,1262961,1262968,1262971,1262969,1262979,1263221,1262980,1263220,1262985,1262981,1263219,1263218,1263209,1262983,1263208,1263216,1263217,1263213,1263215,1263210,1262984,1262982,1263211,1263212,1263214,1263222,1264649,1264648,1263126,1263125,1270021,1263112,1263108,1263124,1263123,1263144,1263122,1263100,1263113,1263121,1263120,1263115,1263111,1263118,1263116,1263109,1263119,1263117,1263127,1263147,1263128,1263129,1263145,1263144,1263146,1263133,1263135,1263143,1263131,1263142,1263139,1263132,1263141,1263134,1263136,1263130,1263137,1263140,1263138,1263148,1264647,1264646,1264429,1264427,1264248,1264435,1264436,1264434,1264246,1264428,1264247,1264439,1264437,1264438,1264426,1264416,1264423,1264422,1264425,1264424,1264421,1264418,1264417,1264420,1264419,1264415,1264250,1264410,1264412,1264411,1264414,1264413,1264254,1264254,1264253,1264255,1270019,1264643,1264639};
//  public static long[] pois = new long[]{1262937,1263196,1262936,1263191,1263260,1263202,1263195,1263194,1263201,1263193,1263200,1262938,1263203,1263199,1263204,1263197,1263205,1263198,1263207,1262935,1263192,1263159,1263160,1263151,1263153,1263169,1263161,1263158,1263157,1263162,1263164,1263155,1263163,1263152,1263154,1263156,1263165,1263167,1263150,1263166,1263168,1263149,1263190,1264652,1264653,1263182,1263180,1263172,1263179,1263174,1263178,1263181,1263183,1263185,1263184,1263173,1263176,1263175,1263177,1263186,1263188,1263189,1263187,1263170,1263171,1263243,1263233,1263225,1263232,1263235,1263227,1263231,1263234,1263236,1263237,1263238,1263226,1263229,1263241,1263228,1263230,1263239,1263240,1263224,1263242,1263223,1263244,1263263,1263262,1263249,1263245,1263251,1263261,1263260,1263247,1263259,1263255,1263248,1263250,1263258,1263257,1263252,1263264,1263254,1263246,1263253,1263256,1264651,1264650,1262978,1262977,1262959,1262964,1262960,1262976,1262975,1262966,1262974,1262962,1262965,1262973,1262970,1262963,1262967,1262972,1262961,1262968,1262971,1262969,1262979,1263221,1262980,1263220,1262985,1262981,1263219,1263218,1263209,1262983,1263208,1263216,1263217,1263213,1263215,1263210,1262984,1262982,1263211,1263212,1263214,1263222,1264649,1264648,1263126,1263125,1270021,1263112,1263108,1263124,1263123,1263144,1263122,1263100,1263113,1263121,1263120,1263115,1263111,1263118,1263116,1263109,1263119,1263117,1263127,1263147,1263128,1263129,1263145,1263144,1263146,1263133,1263135,1263143,1263131,1263142,1263139,1263132,1263141,1263134,1263136,1263130,1263137,1263140,1263138,1263148,1264647,1264646,1263084,1263065,1263066,1263082,1263083,1263081,1263070,1263072,1263071,1263068,1263080,1263079,1263078,1263076,1263069,1263073,1263067,1263074,1263075,1263077,1263085,1263086,1263105,1263087,1263103,1263140,1263102,1263091,1263093,1263092,1263089,1263101,1263100,1263097,1263099,1263090,1263094,1263088,1263095,1263096,1263098,1263106,1264645,1264644,1270027,1263039,1263063,1263061,1263060,1263062,1263044,1263051,1263050,1263042,1263055,1263043,1263052,1263057,1263058,1263053,1263041,1263054,1263056,1263064,1263040,1264689,1264688,1263046,1263045,1264687,1264686,1263048,1263047,1264685,1264191};

public static long[] pois = new  long []{1264693,1263254,1262963,1263244,1263249,1263262,1263263,1263245,1263251,1263261,1263260,1263247,1263259,1263255,1263248,1263250,1263258,1263257,1263252,1263253,1263246,1263245,1263256,1264650,1264651,1263264,1262979,1262969,1262961,1262936,1262970,1262974,1262962,1262964,1262977,1262978,1262959,1262960,1262976,1262975,1262966,1262965,1262967,1262973,1262972,1262968,1262971,1263222,1263212,1262982,1262984,1263213,1263217,1262983,1262985,1263220,1263221,1262980,1262981,1263219,1263218,1263209,1263208,1263216,1263210,1263215,1263211,1263214,1264649,1264648,1270021,1263126,1263125,1263112,1263110,1263122,1263118,1263111,1263109,1263117,1263172,1263108,1263124,1263123,1263114,1263113,1263121,1263115,1263120,1263116,1263119,1263127,1263148,1263138,1263130,1263132,1263139,1263143,1263131,1263133,1263146,1263147,1263128,1263129,1263145,1263144,1263135,1263134,1263142,1263136,1263141,1263137,1263140,1264647,1264646};
}