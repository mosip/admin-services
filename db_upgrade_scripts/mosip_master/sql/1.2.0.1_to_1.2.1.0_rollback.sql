-- Rollback script for master.app_detail
ALTER TABLE master.app_detail ALTER COLUMN lang_code SET NOT NULL;
ALTER TABLE master.app_detail DROP CONSTRAINT pk_appdtl_id;
ALTER TABLE master.app_detail ADD CONSTRAINT pk_appdtl_id PRIMARY KEY (id, lang_code);

-- Rollback script for master.biometric_attribute
ALTER TABLE master.biometric_attribute ALTER COLUMN lang_code SET NOT NULL;
ALTER TABLE master.biometric_attribute DROP CONSTRAINT pk_bmattr_code;
ALTER TABLE master.biometric_attribute ADD CONSTRAINT pk_bmattr_code PRIMARY KEY (code, lang_code);

-- Rollback script for master.module_detail
ALTER TABLE master.module_detail ALTER COLUMN lang_code SET NOT NULL;
ALTER TABLE master.module_detail DROP CONSTRAINT pk_moddtl_code;
ALTER TABLE master.module_detail ADD CONSTRAINT pk_moddtl_code PRIMARY KEY (id, lang_code);

-- Rollback script for master.template_file_format
ALTER TABLE master.template DROP CONSTRAINT IF EXISTS fk_tmplt_tffmt CASCADE;
ALTER TABLE master.template_file_format ALTER COLUMN lang_code SET NOT NULL;
ALTER TABLE master.template_file_format DROP CONSTRAINT pk_tffmt_code;
ALTER TABLE master.template_file_format ADD CONSTRAINT pk_tffmt_code PRIMARY KEY (code, lang_code);

-- Rollback script for master.template
ALTER TABLE master.template ADD CONSTRAINT fk_tmplt_tffmt FOREIGN KEY (file_format_code, lang_code) REFERENCES master.template_file_format (code, lang_code);

DELETE FROM master.template
WHERE id='2002' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2003' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2004' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2005' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2006' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2007' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2008' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2009' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2010' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2011' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2012' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2013' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2014' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2015' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2016' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2017' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2018' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2019' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2020' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2021' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2022' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2023' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2024' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2025' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2026' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2027' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2028' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2029' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2030' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2031' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2032' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2033' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2034' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2035' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2036' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2037' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2038' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2039' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2040' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2041' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2042' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2043' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2044' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2045' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2046' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2083' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2047' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2048' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2049' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2050' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2051' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2052' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2053' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2054' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2055' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2056' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2057' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2058' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2059' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2060' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2061' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2062' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2063' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2064' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2065' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2066' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2067' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2068' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2069' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2070' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2071' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2072' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2073' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2074' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2075' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2076' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2077' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2078' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2079' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2080' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2081' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2082' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2084' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2085' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2086' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2087' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2088' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2089' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2090' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2091' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2092' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2093' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2094' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2095' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2096' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2097' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2098' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2099' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2100' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2101' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2102' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2103' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2104' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2105' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2106' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2107' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2108' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2109' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2110' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2111' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2112' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2192' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2113' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2114' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2115' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2116' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2117' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2118' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2119' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2120' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2121' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2122' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2123' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2124' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2125' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2126' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2127' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2128' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2129' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2130' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2131' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2132' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2133' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2134' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2135' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2136' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2137' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2138' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2139' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2140' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2141' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2142' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2143' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2144' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2145' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2146' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2147' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2148' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2149' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2150' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2151' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2152' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2153' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2154' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2155' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2156' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2157' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2158' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2159' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2160' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2161' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2162' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2163' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2164' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2165' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2166' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2167' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2168' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2169' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2170' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2171' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2172' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2193' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2173' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2174' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2175' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2176' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2177' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2178' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2179' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2180' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2181' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2182' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2183' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2184' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2185' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2186' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2187' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2188' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2189' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2190' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2191' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2194' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2195' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2196' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2197' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2198' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2199' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2200' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2201' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2202' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2203' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2204' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2205' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2206' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2207' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2208' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2209' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2210' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2211' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2212' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2213' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2214' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2215' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2216' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2217' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2218' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2219' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2220' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2221' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2222' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2223' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2224' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2225' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2226' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2227' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2228' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2229' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2230' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2231' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2232' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2233' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2234' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2235' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2236' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2237' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2238' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2239' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2240' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2241' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2242' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2243' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2244' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2245' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2246' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2247' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2248' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2249' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2250' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2251' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2252' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2253' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2254' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2255' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2256' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2257' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2258' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2259' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2260' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2261' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2262' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2263' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2264' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2265' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2266' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2267' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2268' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2269' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2270' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2271' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2272' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2273' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2274' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2275' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2276' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2277' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2278' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2279' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2280' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2281' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2282' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2283' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2284' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2285' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2286' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2287' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2288' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2289' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2290' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2291' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2292' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2293' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2294' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2295' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2296' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2297' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2298' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2299' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2300' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2301' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2741' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2302' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2303' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2304' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2305' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2306' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2307' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2308' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2309' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2310' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2311' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2312' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2313' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2314' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2315' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2316' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2317' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2318' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2319' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2320' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2321' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2322' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2323' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2324' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2325' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2326' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2327' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2328' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2329' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2330' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2362' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2331' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2332' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2333' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2334' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2335' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2336' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2337' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2338' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2339' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2340' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2341' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2342' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2343' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2344' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2345' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2346' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2347' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2348' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2349' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2350' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2351' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2352' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2353' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2354' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2355' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2356' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2357' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2358' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2359' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2360' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2361' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2363' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2364' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2365' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2366' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2367' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2368' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2369' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2370' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2371' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2372' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2373' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2374' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2375' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2376' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2377' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2378' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2379' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2380' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2381' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2382' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2383' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2384' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2385' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2386' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2387' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2388' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2389' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2390' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2391' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2392' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2393' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2394' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2395' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2396' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2397' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2398' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2399' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2400' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2401' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2402' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2403' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2404' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2405' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2406' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2407' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2408' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2409' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2410' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2411' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2412' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2413' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2414' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2415' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2416' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2417' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2418' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2419' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2420' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2421' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2422' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2423' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2424' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2425' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2426' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2427' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2428' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2429' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2430' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2431' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2432' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2433' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2434' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2435' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2436' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2437' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2438' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2439' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2440' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2441' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2442' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2443' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2444' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2445' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2446' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2447' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2448' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2449' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2450' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2451' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2452' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2487' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2453' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2454' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2455' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2456' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2457' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2458' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2459' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2460' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2461' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2462' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2463' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2464' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2465' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2466' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2467' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2468' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2469' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2470' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2471' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2472' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2473' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2474' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2475' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2476' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2477' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2478' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2479' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2480' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2481' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2482' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2483' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2484' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2485' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2486' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2488' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2489' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2490' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2491' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2492' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2493' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2494' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2495' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2496' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2497' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2498' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2499' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2500' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2501' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2502' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2503' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2504' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2505' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2506' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2507' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2508' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2509' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2510' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2511' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2512' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2513' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2514' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2515' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2516' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2517' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2518' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2519' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2520' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2521' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2522' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2523' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2524' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2525' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2526' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2527' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2528' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2529' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2530' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2531' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2532' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2533' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2534' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2535' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2536' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2537' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2538' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2539' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2540' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2541' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2542' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2543' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2544' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2545' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2546' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2547' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2548' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2549' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2550' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2551' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2552' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2553' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2554' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2555' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2556' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2557' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2558' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2559' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2560' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2561' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2562' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2563' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2564' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2565' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2566' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2567' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2568' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2569' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2570' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2571' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2572' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2573' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2574' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2575' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2576' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2577' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2578' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2579' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2580' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2581' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2582' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2583' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2584' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2585' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2586' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2587' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2588' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2589' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2590' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2591' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2592' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2593' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2594' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2595' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2596' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2597' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2598' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2599' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2600' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2601' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2602' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2603' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2604' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2605' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2606' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2607' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2608' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2609' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2610' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2611' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2612' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2613' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2614' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2615' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2616' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2617' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2618' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2619' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2620' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2621' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2622' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2623' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2624' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2625' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2626' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2627' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2628' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2629' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2630' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2631' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2632' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2633' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2634' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2635' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2636' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2637' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2638' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2639' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2640' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2641' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2642' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2643' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2644' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2645' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2646' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2647' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2648' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2649' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2650' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2651' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2652' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2653' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2654' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2655' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2656' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2657' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2658' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2659' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2660' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2661' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2662' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2663' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2664' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2665' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2666' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2667' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2668' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2669' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2670' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2671' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2672' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2673' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2674' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2675' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2676' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2677' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2678' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2679' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2680' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2681' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2682' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2683' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2684' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2685' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2686' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2687' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2688' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2689' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2690' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2691' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2692' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2693' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2694' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2695' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2696' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2697' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2698' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2699' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2700' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2701' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2702' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2703' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2704' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2705' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2706' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2707' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2708' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2709' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2710' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2711' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2712' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2713' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2714' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2715' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2716' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2717' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2718' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2719' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2720' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2721' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2722' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2723' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2724' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2725' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2726' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2727' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2728' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2729' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2730' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2731' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2732' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2733' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2734' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2735' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2736' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2737' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2738' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2739' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2740' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2742' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2743' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2744' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2745' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2746' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2747' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2748' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2749' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2750' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2751' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2752' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2753' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2754' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2755' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2756' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2757' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2758' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2759' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2760' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2761' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2762' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2846' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2763' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2764' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2765' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2766' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2767' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2850' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2768' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2769' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2770' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2771' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2847' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2848' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2849' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2772' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2773' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2774' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2775' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2789' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2776' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2777' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2778' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2779' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2796' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2780' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2781' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2782' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2783' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2797' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2784' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2785' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2786' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2787' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2788' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2833' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2834' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2790' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2791' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2792' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2793' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2794' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2795' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2798' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2799' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2800' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2801' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2802' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2835' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2836' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2837' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2838' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2839' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2840' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2803' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2804' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2805' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2806' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2807' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2841' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2842' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2808' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2809' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2810' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2811' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2812' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2843' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2813' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2814' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2815' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2816' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2817' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2844' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2845' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2818' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2819' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2820' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2821' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2822' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2823' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2824' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2825' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2826' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2827' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2828' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2829' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2830' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2831' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2832' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2851' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2852' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2853' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2854' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2855' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2856' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2857' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2858' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2859' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2860' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2861' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2862' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2863' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2864' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2865' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2916' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2866' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2867' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2868' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2869' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2870' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2871' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2872' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2873' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2874' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2875' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2876' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2877' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2893' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2878' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2879' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2880' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2881' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2882' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2883' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2884' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2885' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2886' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2887' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2888' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2889' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2890' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2891' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2892' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2894' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2895' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2896' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2897' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2898' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2899' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2900' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2901' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2902' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2903' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2904' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2905' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2906' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2907' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2908' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2909' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2910' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2911' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2912' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2913' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2914' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2915' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2917' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2918' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2919' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2920' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2921' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2922' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2923' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2924' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2925' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2926' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2927' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2928' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2929' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2930' AND lang_code='ara';
DELETE FROM master.template
WHERE id='2931' AND lang_code='eng';
DELETE FROM master.template
WHERE id='2932' AND lang_code='spa';
DELETE FROM master.template
WHERE id='2933' AND lang_code='kan';
DELETE FROM master.template
WHERE id='2934' AND lang_code='tam';
DELETE FROM master.template
WHERE id='2935' AND lang_code='hin';
DELETE FROM master.template
WHERE id='2936' AND lang_code='fra';
DELETE FROM master.template
WHERE id='2937' AND lang_code='ara';

DELETE FROM master.template_type
WHERE code='RS_AUTH_HIST_SUCCESS_EMAIL' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_AUTH_HIST_SUCCESS_SMS' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_AUTH_HIST_SUCCESS_EMAIL_SUB' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_DOW_UIN_SUCCESS_EMAIL' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_DOW_UIN_SUCCESS_EMAIL_SUB' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_DOW_UIN_SUCCESS_SMS' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_LOCK_AUTH_SUCCESS_EMAIL' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_LOCK_AUTH_SUCCESS_EMAIL_SUB' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_LOCK_AUTH_SUCCESS_SMS' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_UNLOCK_AUTH_SUCCESS_EMAIL' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_UNLOCK_AUTH_SUCCESS_EMAIL_SUB' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_UNLOCK_AUTH_SUCCESS_SMS' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_VIN_GEN_SUCCESS_EMAIL' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_VIN_GEN_SUCCESS_EMAIL_SUB' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_VIN_GEN_SUCCESS_SMS' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_VIN_REV_SUCCESS_EMAIL' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_VIN_REV_SUCCESS_EMAIL_SUB' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_VIN_REV_SUCCESS_SMS' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_UIN_RPR_SUCCESS_EMAIL' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_UIN_RPR_SUCCESS_EMAIL_SUB' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_UIN_RPR_SUCCESS_SMS' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_AUTH_HIST_FAILURE_EMAIL' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_AUTH_HIST_FAILURE_EMAIL_SUB' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_AUTH_HIST_FAILURE_SMS' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_DOW_UIN_FAILURE_EMAIL' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_DOW_UIN_FAILURE_EMAIL_SUB' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_DOW_UIN_FAILURE_SMS' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_LOCK_AUTH_FAILURE_EMAIL' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_LOCK_AUTH_FAILURE_EMAIL_SUB' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_LOCK_AUTH_FAILURE_SMS' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_UIN_RPR_FAILURE_EMAIL' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_UIN_RPR_FAILURE_EMAIL_SUB' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_UIN_RPR_FAILURE_SMS' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_UNLOCK_AUTH_FAILURE_EMAIL' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_UNLOCK_AUTH_FAILURE_EMAIL_SUB' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_UNLOCK_AUTH_FAILURE_SMS' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_VIN_GEN_FAILURE_EMAIL' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_VIN_GEN_FAILURE_EMAIL_SUB' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_VIN_GEN_FAILURE_SMS' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_VIN_REV_FAILURE_EMAIL' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_VIN_REV_FAILURE_EMAIL_SUB' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_VIN_REV_FAILURE_SMS' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_UIN_UPDATE_SUCCESS_EMAIL' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_UIN_UPDATE_SUCCESS_EMAIL_SUB' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_UIN_UPDATE_SUCCESS_SMS' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_UIN_UPDATE_FAILURE_EMAIL' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_UIN_UPDATE_FAILURE_EMAIL_SUB' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_UIN_UPDATE_FAILURE_SMS' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_CRE_REQ_SUCCESS_SMS' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_CRE_REQ_SUCCESS_EMAIL' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_CRE_REQ_SUCCESS_EMAIL_SUB' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_CRE_STATUS_SMS' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_CRE_STATUS_EMAIL' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_CRE_STATUS_EMAIL_SUB' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_CRE_CANCEL_SUCCESS_SMS' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_CRE_CANCEL_SUCCESS_EMAIL' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_CRE_CANCEL_SUCCESS_EMAIL_SUB' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_CRE_REQ_FAILURE_SMS' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_CRE_REQ_FAILURE_EMAIL' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_CRE_REQ_FAILURE_EMAIL_SUB' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='RS_AUTH_HIST_SUCCESS_EMAIL' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_AUTH_HIST_SUCCESS_SMS' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_AUTH_HIST_SUCCESS_EMAIL_SUB' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_DOW_UIN_SUCCESS_EMAIL' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_DOW_UIN_SUCCESS_EMAIL_SUB' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_DOW_UIN_SUCCESS_SMS' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_LOCK_AUTH_SUCCESS_EMAIL' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_LOCK_AUTH_SUCCESS_EMAIL_SUB' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_LOCK_AUTH_SUCCESS_SMS' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_UNLOCK_AUTH_SUCCESS_EMAIL' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_UNLOCK_AUTH_SUCCESS_EMAIL_SUB' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_UNLOCK_AUTH_SUCCESS_SMS' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_VIN_GEN_SUCCESS_EMAIL' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_VIN_GEN_SUCCESS_EMAIL_SUB' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_VIN_GEN_SUCCESS_SMS' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_VIN_REV_SUCCESS_EMAIL' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_VIN_REV_SUCCESS_EMAIL_SUB' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_VIN_REV_SUCCESS_SMS' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_UIN_RPR_SUCCESS_EMAIL' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_UIN_RPR_SUCCESS_EMAIL_SUB' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_UIN_RPR_SUCCESS_SMS' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_AUTH_HIST_FAILURE_EMAIL' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_AUTH_HIST_FAILURE_EMAIL_SUB' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_AUTH_HIST_FAILURE_SMS' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_DOW_UIN_FAILURE_EMAIL' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_DOW_UIN_FAILURE_EMAIL_SUB' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_DOW_UIN_FAILURE_SMS' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_LOCK_AUTH_FAILURE_EMAIL' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_LOCK_AUTH_FAILURE_EMAIL_SUB' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_LOCK_AUTH_FAILURE_SMS' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_UIN_RPR_FAILURE_EMAIL' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_UIN_RPR_FAILURE_EMAIL_SUB' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_UIN_RPR_FAILURE_SMS' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_UNLOCK_AUTH_FAILURE_EMAIL' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_UNLOCK_AUTH_FAILURE_EMAIL_SUB' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_UNLOCK_AUTH_FAILURE_SMS' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_VIN_GEN_FAILURE_EMAIL' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_VIN_GEN_FAILURE_EMAIL_SUB' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_VIN_GEN_FAILURE_SMS' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_VIN_REV_FAILURE_EMAIL' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_VIN_REV_FAILURE_EMAIL_SUB' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_VIN_REV_FAILURE_SMS' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_UIN_UPDATE_SUCCESS_EMAIL' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_UIN_UPDATE_SUCCESS_EMAIL_SUB' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_UIN_UPDATE_SUCCESS_SMS' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_UIN_UPDATE_FAILURE_EMAIL' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_UIN_UPDATE_FAILURE_EMAIL_SUB' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_UIN_UPDATE_FAILURE_SMS' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_CRE_REQ_SUCCESS_SMS' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_CRE_REQ_SUCCESS_EMAIL' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_CRE_REQ_SUCCESS_EMAIL_SUB' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_CRE_STATUS_SMS' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_CRE_STATUS_EMAIL' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_CRE_STATUS_EMAIL_SUB' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_CRE_CANCEL_SUCCESS_SMS' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_CRE_CANCEL_SUCCESS_EMAIL' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_CRE_CANCEL_SUCCESS_EMAIL_SUB' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_CRE_REQ_FAILURE_SMS' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_CRE_REQ_FAILURE_EMAIL' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_CRE_REQ_FAILURE_EMAIL_SUB' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='RS_AUTH_HIST_SUCCESS_EMAIL' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_AUTH_HIST_SUCCESS_SMS' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_AUTH_HIST_SUCCESS_EMAIL_SUB' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_DOW_UIN_SUCCESS_EMAIL' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_DOW_UIN_SUCCESS_EMAIL_SUB' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_DOW_UIN_SUCCESS_SMS' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_LOCK_AUTH_SUCCESS_EMAIL' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_LOCK_AUTH_SUCCESS_EMAIL_SUB' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_LOCK_AUTH_SUCCESS_SMS' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_UNLOCK_AUTH_SUCCESS_EMAIL' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_UNLOCK_AUTH_SUCCESS_EMAIL_SUB' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_UNLOCK_AUTH_SUCCESS_SMS' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_VIN_GEN_SUCCESS_EMAIL' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_VIN_GEN_SUCCESS_EMAIL_SUB' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_VIN_GEN_SUCCESS_SMS' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_VIN_REV_SUCCESS_EMAIL' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_VIN_REV_SUCCESS_EMAIL_SUB' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_VIN_REV_SUCCESS_SMS' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_UIN_RPR_SUCCESS_EMAIL' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_UIN_RPR_SUCCESS_EMAIL_SUB' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_UIN_RPR_SUCCESS_SMS' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_AUTH_HIST_FAILURE_EMAIL' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_AUTH_HIST_FAILURE_EMAIL_SUB' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_AUTH_HIST_FAILURE_SMS' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_DOW_UIN_FAILURE_EMAIL' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_DOW_UIN_FAILURE_EMAIL_SUB' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_DOW_UIN_FAILURE_SMS' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_LOCK_AUTH_FAILURE_EMAIL' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_LOCK_AUTH_FAILURE_EMAIL_SUB' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_LOCK_AUTH_FAILURE_SMS' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_UIN_RPR_FAILURE_EMAIL' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_UIN_RPR_FAILURE_EMAIL_SUB' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_UIN_RPR_FAILURE_SMS' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_UNLOCK_AUTH_FAILURE_EMAIL' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_UNLOCK_AUTH_FAILURE_EMAIL_SUB' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_UNLOCK_AUTH_FAILURE_SMS' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_VIN_GEN_FAILURE_EMAIL' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_VIN_GEN_FAILURE_EMAIL_SUB' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_VIN_GEN_FAILURE_SMS' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_VIN_REV_FAILURE_EMAIL' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_VIN_REV_FAILURE_EMAIL_SUB' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_VIN_REV_FAILURE_SMS' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_UIN_UPDATE_SUCCESS_EMAIL' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_UIN_UPDATE_SUCCESS_EMAIL_SUB' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_UIN_UPDATE_SUCCESS_SMS' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_UIN_UPDATE_FAILURE_EMAIL' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_UIN_UPDATE_FAILURE_EMAIL_SUB' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_UIN_UPDATE_FAILURE_SMS' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_CRE_REQ_SUCCESS_SMS' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_CRE_REQ_SUCCESS_EMAIL' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_CRE_REQ_SUCCESS_EMAIL_SUB' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_CRE_STATUS_SMS' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_CRE_STATUS_EMAIL' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_CRE_STATUS_EMAIL_SUB' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_CRE_CANCEL_SUCCESS_SMS' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_CRE_CANCEL_SUCCESS_EMAIL' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_CRE_CANCEL_SUCCESS_EMAIL_SUB' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_CRE_REQ_FAILURE_SMS' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_CRE_REQ_FAILURE_EMAIL' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_CRE_REQ_FAILURE_EMAIL_SUB' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='RS_AUTH_HIST_SUCCESS_EMAIL' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='RS_AUTH_HIST_SUCCESS_SMS' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='RS_AUTH_HIST_SUCCESS_EMAIL_SUB' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='RS_DOW_UIN_SUCCESS_EMAIL' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='RS_DOW_UIN_SUCCESS_EMAIL_SUB' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='RS_DOW_UIN_SUCCESS_SMS' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='RS_LOCK_AUTH_SUCCESS_EMAIL' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='RS_LOCK_AUTH_SUCCESS_EMAIL_SUB' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='RS_LOCK_AUTH_SUCCESS_SMS' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='RS_UNLOCK_AUTH_SUCCESS_EMAIL' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='RS_UNLOCK_AUTH_SUCCESS_EMAIL_SUB' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='RS_UNLOCK_AUTH_SUCCESS_SMS' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='RS_VIN_GEN_SUCCESS_EMAIL' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='RS_VIN_GEN_SUCCESS_EMAIL_SUB' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='RS_VIN_GEN_SUCCESS_SMS' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='RS_VIN_REV_SUCCESS_EMAIL' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='RS_VIN_REV_SUCCESS_EMAIL_SUB' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='RS_VIN_REV_SUCCESS_SMS' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='RS_UIN_RPR_SUCCESS_EMAIL' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='RS_UIN_RPR_SUCCESS_EMAIL_SUB' AND lang_code='kan';