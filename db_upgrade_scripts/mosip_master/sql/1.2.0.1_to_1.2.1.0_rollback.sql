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

UPDATE master.template
SET "name"='ದೃಢೀಕರಣ ಇತಿಹಾಸ ವಿನಂತಿಯ ಯಶಸ್ಸಿನ ಇಮೇಲ್', descr='ದೃಢೀಕರಣ ಇತಿಹಾಸ ವಿನಂತಿಯ ಯಶಸ್ಸಿನ ಇಮೇಲ್', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ದೃಢೀಕರಣ ಇತಿಹಾಸಕ್ಕಾಗಿ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಪ್ರಕ್ರಿಯೆಗೊಳಿಸಲಾಗಿದೆ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_AUTH_HIST_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1163' AND lang_code='kan';
UPDATE master.template
SET "name"='ईमेल पुष्टिकरण इतिहास अनुरोध सफलता', descr='ईमेल पुष्टिकरण इतिहास अनुरोध सफलता', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, प्रमाणीकरण इतिहास के लिए आपका अनुरोध सफलतापूर्वक संसाधित कर दिया गया है। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_AUTH_HIST_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1163' AND lang_code='hin';
UPDATE master.template
SET "name"='E-mail de réussite de la demande d''historique d''authentification', descr='E-mail de réussite de la demande d''historique d''authentification', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre demande d''historique d''authentification a été traitée avec succès. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_AUTH_HIST_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1163' AND lang_code='fra';
UPDATE master.template
SET "name"='سجل المصادقة طلب البريد الإلكتروني بنجاح', descr='سجل المصادقة طلب البريد الإلكتروني بنجاح', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، تمت معالجة طلبك الخاص بسجل المصادقة بنجاح. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_AUTH_HIST_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1163' AND lang_code='ara';
UPDATE master.template
SET "name"='மின்னஞ்சல் உறுதிப்படுத்தல் வரலாறு கோரிக்கை வெற்றி', descr='மின்னஞ்சல் உறுதிப்படுத்தல் வரலாறு கோரிக்கை வெற்றி', file_format_code='txt', model='velocity', file_txt='ஹாய் $ fullName_eng, அங்கீகரிப்பு வரலாற்றிற்கான உங்கள் கோரிக்கை வெற்றிகரமாக செயலாக்கப்பட்டது. நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_AUTH_HIST_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1163' AND lang_code='tam';
UPDATE master.template
SET "name"='Authentication History Request Success Email', descr='Authentication History Request Success Email', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your Request for Authentication History has been processed successfully. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_AUTH_HIST_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1163' AND lang_code='eng';
UPDATE master.template
SET "name"='அங்கீகார வரலாறு கோரிக்கை வெற்றி SMS', descr='அங்கீகார வரலாறு கோரிக்கை வெற்றி SMS', file_format_code='txt', model='velocity', file_txt='ஹாய் $ fullName_eng, அங்கீகரிப்பு வரலாற்றிற்கான உங்கள் கோரிக்கை வெற்றிகரமாக செயலாக்கப்பட்டது. நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_AUTH_HIST_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1164' AND lang_code='tam';
UPDATE master.template
SET "name"='Authentication History Request Success SMS', descr='Authentication History Request Success SMS', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your Request for Authentication History has been processed successfully. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_AUTH_HIST_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1164' AND lang_code='eng';
UPDATE master.template
SET "name"='प्रमाणीकरण इतिहास अनुरोध सफलता एसएमएस', descr='प्रमाणीकरण इतिहास अनुरोध सफलता एसएमएस', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, प्रमाणीकरण इतिहास के लिए आपका अनुरोध सफलतापूर्वक संसाधित कर दिया गया है। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_AUTH_HIST_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1164' AND lang_code='hin';
UPDATE master.template
SET "name"='Historique de l''authentification des services résidents SMS de transcation', descr='SMS de réussite de la demande d''historique d''authentification', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre demande d''historique d''authentification a été traitée avec succès. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_AUTH_HIST_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1164' AND lang_code='fra';
UPDATE master.template
SET "name"='ದೃಢೀಕರಣ ಇತಿಹಾಸ ವಿನಂತಿ ಯಶಸ್ಸಿನ SMS', descr='ದೃಢೀಕರಣ ಇತಿಹಾಸ ವಿನಂತಿ ಯಶಸ್ಸಿನ SMS', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ದೃಢೀಕರಣ ಇತಿಹಾಸಕ್ಕಾಗಿ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಪ್ರಕ್ರಿಯೆಗೊಳಿಸಲಾಗಿದೆ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_AUTH_HIST_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1164' AND lang_code='kan';
UPDATE master.template
SET "name"='تاريخ المصادقة طلب نجاح SMS', descr='تاريخ المصادقة طلب نجاح SMS', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، تمت معالجة طلبك الخاص بسجل المصادقة بنجاح. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_AUTH_HIST_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1164' AND lang_code='ara';
UPDATE master.template
SET "name"='உறுதிப்படுத்தல் வரலாறு என்பது கோரிக்கை மின்னஞ்சல் வெற்றியின் பொருள்', descr='உறுதிப்படுத்தல் வரலாறு என்பது கோரிக்கை மின்னஞ்சல் வெற்றியின் பொருள்', file_format_code='txt', model='velocity', file_txt='அங்கீகரிப்பு வரலாறு கோரிக்கை வெற்றி பெற்றது', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_AUTH_HIST_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1165' AND lang_code='tam';
UPDATE master.template
SET "name"='Authentication History Request Success EMAIL Subject', descr='Authentication History Request Success EMAIL Subject', file_format_code='txt', model='velocity', file_txt='Authentication History Request Success', module_id='10006', module_name='Resident Services', template_typ_code='RS_AUTH_HIST_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1165' AND lang_code='eng';
UPDATE master.template
SET "name"='Succès EMAIL de la demande d''historique d''authentification', descr='Succès EMAIL de la demande d''historique d''authentification', file_format_code='txt', model='velocity', file_txt='Demande d''historique d''authentification', module_id='10006', module_name='Resident Services', template_typ_code='RS_AUTH_HIST_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1165' AND lang_code='fra';
UPDATE master.template
SET "name"='تاريخ المصادقة طلب نجاح EMAIL الموضوع', descr='تاريخ المصادقة طلب نجاح EMAIL الموضوع', file_format_code='txt', model='velocity', file_txt='نجاح طلب محفوظات المصادقة', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_AUTH_HIST_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1165' AND lang_code='ara';
UPDATE master.template
SET "name"='ದೃಢೀಕರಣ ಇತಿಹಾಸ ವಿನಂತಿಯ ಯಶಸ್ಸಿನ ಇಮೇಲ್ ವಿಷಯ', descr='ದೃಢೀಕರಣ ಇತಿಹಾಸ ವಿನಂತಿಯ ಯಶಸ್ಸಿನ ಇಮೇಲ್ ವಿಷಯ', file_format_code='txt', model='velocity', file_txt='ದೃಢೀಕರಣ ಇತಿಹಾಸ ವಿನಂತಿ ಯಶಸ್ವಿಯಾಗಿದೆ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_AUTH_HIST_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1165' AND lang_code='kan';
UPDATE master.template
SET "name"='पुष्टिकरण इतिहास एक अनुरोध ईमेल सफलता का विषय है', descr='पुष्टिकरण इतिहास एक अनुरोध ईमेल सफलता का विषय है', file_format_code='txt', model='velocity', file_txt='प्रमाणीकरण इतिहास का अनुरोध सफल रहा', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_AUTH_HIST_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1165' AND lang_code='hin';
UPDATE master.template
SET "name"='Téléchargement réussi du courriel e-UIN', descr='Téléchargement réussi du courriel e-UIN', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Félicitations, votre demande est traitée. La demande de téléchargement de votre e-UIN a abouti. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_DOW_UIN_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1166' AND lang_code='fra';
UPDATE master.template
SET "name"='E-UIN மின்னஞ்சலின் வெற்றிகரமான பதிவிறக்கம்', descr='E-UIN மின்னஞ்சலின் வெற்றிகரமான பதிவிறக்கம்', file_format_code='txt', model='velocity', file_txt='ஹாய் $ fullName_eng, வாழ்த்துகள் உங்கள் கோரிக்கை செயல்படுத்தப்பட்டது. உங்கள் e-UINஐப் பதிவிறக்குவதற்கான கோரிக்கை வெற்றியடைந்தது. நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_DOW_UIN_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1166' AND lang_code='tam';
UPDATE master.template
SET "name"='ई-यूआईएन ईमेल का सफल डाउनलोड', descr='ई-यूआईएन ईमेल का सफल डाउनलोड', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, बधाई हो आपका अनुरोध संसाधित कर दिया गया है। आपका ई-यूआईएन डाउनलोड करने का अनुरोध सफल रहा। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_DOW_UIN_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1166' AND lang_code='hin';
UPDATE master.template
SET "name"='Successful Download of e-UIN Email', descr='Successful Download of e-UIN Email', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Congratulations your request is processed. The request for downloading your e-UIN is Successful. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_DOW_UIN_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1166' AND lang_code='eng';
UPDATE master.template
SET "name"='ಇ-ಯುಐಎನ್ ಇಮೇಲ್‌ನ ಯಶಸ್ವಿ ಡೌನ್‌ಲೋಡ್', descr='ಇ-ಯುಐಎನ್ ಇಮೇಲ್‌ನ ಯಶಸ್ವಿ ಡೌನ್‌ಲೋಡ್', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ಅಭಿನಂದನೆಗಳು ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು ಪ್ರಕ್ರಿಯೆಗೊಳಿಸಲಾಗಿದೆ. ನಿಮ್ಮ e-UIN ಅನ್ನು ಡೌನ್‌ಲೋಡ್ ಮಾಡುವ ವಿನಂತಿಯು ಯಶಸ್ವಿಯಾಗಿದೆ. ಧನ್ಯವಾದಗಳು! ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_DOW_UIN_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1166' AND lang_code='kan';
UPDATE master.template
SET "name"='تنزيل ناجح للبريد الإلكتروني e-UIN', descr='تنزيل ناجح للبريد الإلكتروني e-UIN', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، تهانينا ، تم معالجة طلبك. تم طلب تنزيل e-UIN الخاص بك بنجاح. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_DOW_UIN_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1166' AND lang_code='ara';
UPDATE master.template
SET "name"='تنزيل ناجح لموضوع البريد الإلكتروني لـ e-UIN', descr='قم بتنزيل موضوع البريد الإلكتروني الخاص بحالة e-UIN', file_format_code='txt', model='velocity', file_txt='تم تنزيل e-UIN بنجاح', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_DOW_UIN_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1167' AND lang_code='ara';
UPDATE master.template
SET "name"='Successful Download of e-UIN Email Subject', descr='Download e-UIN Status Email Subject', file_format_code='txt', model='velocity', file_txt='Download e-UIN is Successful', module_id='10006', module_name='Resident Services', template_typ_code='RS_DOW_UIN_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1167' AND lang_code='eng';
UPDATE master.template
SET "name"='Téléchargement réussi de l''objet de l''e-UIN', descr='Téléchargement réussi de l''objet de l''e-UIN', file_format_code='txt', model='velocity', file_txt='Télécharger e-UIN est réussi', module_id='10006', module_name='Resident Services', template_typ_code='RS_DOW_UIN_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1167' AND lang_code='fra';
UPDATE master.template
SET "name"='e-UIN மின்னஞ்சல் உள்ளடக்கத்தின் வெற்றிகரமான பதிவிறக்கம்', descr='e-UIN மின்னஞ்சல் உள்ளடக்கத்தின் வெற்றிகரமான பதிவிறக்கம்', file_format_code='txt', model='velocity', file_txt='E-UIN பதிவிறக்கம் வெற்றிகரமாக உள்ளது', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_DOW_UIN_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1167' AND lang_code='tam';
UPDATE master.template
SET "name"='ई-यूआईएन ईमेल सामग्री का सफल डाउनलोड', descr='ई-यूआईएन ईमेल सामग्री का सफल डाउनलोड', file_format_code='txt', model='velocity', file_txt='ई-यूआईएन डाउनलोड सफल', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_DOW_UIN_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1167' AND lang_code='hin';
UPDATE master.template
SET "name"='ಇ-ಯುಐಎನ್ ಇಮೇಲ್ ವಿಷಯದ ಯಶಸ್ವಿ ಡೌನ್‌ಲೋಡ್', descr='ಇ-ಯುಐಎನ್ ಇಮೇಲ್ ವಿಷಯದ ಯಶಸ್ವಿ ಡೌನ್‌ಲೋಡ್', file_format_code='txt', model='velocity', file_txt='ಇ-ಯುಐಎನ್ ಡೌನ್‌ಲೋಡ್ ಯಶಸ್ವಿಯಾಗಿದೆ ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_DOW_UIN_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1167' AND lang_code='kan';
UPDATE master.template
SET "name"='ई-यूआईएन एसएमएस का सफल डाउनलोड', descr='ई-यूआईएन एसएमएस का सफल डाउनलोड', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, बधाई हो आपका अनुरोध संसाधित कर दिया गया है। आपका ई-यूआईएन डाउनलोड करने का अनुरोध सफल रहा। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_DOW_UIN_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1168' AND lang_code='hin';
UPDATE master.template
SET "name"='e-UIN SMS இன் வெற்றிகரமான பதிவிறக்கம்', descr='e-UIN SMS இன் வெற்றிகரமான பதிவிறக்கம்', file_format_code='txt', model='velocity', file_txt='ஹாய் $ fullName_eng, வாழ்த்துகள் உங்கள் கோரிக்கை செயல்படுத்தப்பட்டது. உங்கள் e-UINஐப் பதிவிறக்குவதற்கான கோரிக்கை வெற்றியடைந்தது. நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_DOW_UIN_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1168' AND lang_code='tam';
UPDATE master.template
SET "name"='e-UIN SMS ನ ಯಶಸ್ವಿ ಡೌನ್‌ಲೋಡ್', descr='e-UIN SMS ನ ಯಶಸ್ವಿ ಡೌನ್‌ಲೋಡ್', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ಅಭಿನಂದನೆಗಳು ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು ಪ್ರಕ್ರಿಯೆಗೊಳಿಸಲಾಗಿದೆ. ನಿಮ್ಮ e-UIN ಅನ್ನು ಡೌನ್‌ಲೋಡ್ ಮಾಡುವ ವಿನಂತಿಯು ಯಶಸ್ವಿಯಾಗಿದೆ. ಧನ್ಯವಾದಗಳು! ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_DOW_UIN_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1168' AND lang_code='kan';
UPDATE master.template
SET "name"='Téléchargement réussi de l''e-UIN SMS', descr='Téléchargement réussi de l''e-UIN SMS', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Félicitations, votre demande est traitée. La demande de téléchargement de votre e-UIN a abouti. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_DOW_UIN_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1168' AND lang_code='fra';
UPDATE master.template
SET "name"='Successful Download of e-UIN SMS', descr='Successful Download of e-UIN SMS', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Congratulations your request is processed. The request for downloading your e-UIN is Successful. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_DOW_UIN_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1168' AND lang_code='eng';
UPDATE master.template
SET "name"='تنزيل ناجح لرسائل SMS e-UIN', descr='تنزيل ناجح لرسائل SMS e-UIN', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، تهانينا ، تم معالجة طلبك. تم طلب تنزيل e-UIN الخاص بك بنجاح. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_DOW_UIN_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1168' AND lang_code='ara';
UPDATE master.template
SET "name"='Verrouillage réussi des e-mails des types d''authentification', descr='Verrouillage réussi des e-mails des types d''authentification', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Le ou les types AUTH que vous avez demandés ont été verrouillés avec succès. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_LOCK_AUTH_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1169' AND lang_code='fra';
UPDATE master.template
SET "name"='Successful Locking of Auth Types Email', descr='Successful Locking of Auth Types Email', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your requested AUTH type(s) have been locked successfully. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_LOCK_AUTH_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1169' AND lang_code='eng';
UPDATE master.template
SET "name"='ದೃಢೀಕರಣ ಪ್ರಕಾರಗಳ ಇಮೇಲ್‌ನ ಯಶಸ್ವಿ ಲಾಕ್', descr='ದೃಢೀಕರಣ ಪ್ರಕಾರಗಳ ಇಮೇಲ್‌ನ ಯಶಸ್ವಿ ಲಾಕ್', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ನೀವು ವಿನಂತಿಸಿದ AUTH ಪ್ರಕಾರ(ಗಳನ್ನು) ಯಶಸ್ವಿಯಾಗಿ ಲಾಕ್ ಮಾಡಲಾಗಿದೆ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_LOCK_AUTH_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1169' AND lang_code='kan';
UPDATE master.template
SET "name"='உறுதிப்படுத்தல் வகைகளின் மின்னஞ்சல் வெற்றிகரமான பூட்டு', descr='உறுதிப்படுத்தல் வகைகளின் மின்னஞ்சல் வெற்றிகரமான பூட்டு', file_format_code='txt', model='velocity', file_txt='ஹாய் $ fullName_eng, கோரப்பட்ட AUTH வகையை (களை) வெற்றிகரமாகப் பூட்டிவிட்டீர்கள். நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_LOCK_AUTH_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1169' AND lang_code='tam';
UPDATE master.template
SET "name"='पुष्टिकरण प्रकार ईमेल का सफल लॉक', descr='पुष्टिकरण प्रकार ईमेल का सफल लॉक', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, आपने अनुरोधित AUTH प्रकार (प्रकारों) को सफलतापूर्वक लॉक कर दिया है। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_LOCK_AUTH_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1169' AND lang_code='hin';
UPDATE master.template
SET "name"='القفل الناجح لأنواع البريد الإلكتروني للمصادقة', descr='القفل الناجح لأنواع البريد الإلكتروني للمصادقة', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، تم تأمين نوع (أنواع) AUTH المطلوبة بنجاح. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_LOCK_AUTH_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1169' AND lang_code='ara';
UPDATE master.template
SET "name"='قفل ناجح لأنواع المصادقة موضوع البريد الإلكتروني', descr='قفل ناجح لأنواع المصادقة موضوع البريد الإلكتروني', file_format_code='txt', model='velocity', file_txt='تم تأمين نوع (أنواع) AUTH بنجاح', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_LOCK_AUTH_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1170' AND lang_code='ara';
UPDATE master.template
SET "name"='प्रमाणीकरण प्रकारों के लिए ईमेल सामग्री का सफल लॉक', descr='प्रमाणीकरण प्रकारों के लिए ईमेल सामग्री का सफल लॉक', file_format_code='txt', model='velocity', file_txt='AUTH प्रकार सफलतापूर्वक लॉक हो गया है', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_LOCK_AUTH_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1170' AND lang_code='hin';
UPDATE master.template
SET "name"='ದೃಢೀಕರಣ ಪ್ರಕಾರಗಳ ಇಮೇಲ್ ವಿಷಯದ ಯಶಸ್ವಿ ಲಾಕ್ ', descr='ದೃಢೀಕರಣ ಪ್ರಕಾರಗಳ ಇಮೇಲ್ ವಿಷಯದ ಯಶಸ್ವಿ ಲಾಕ್', file_format_code='txt', model='velocity', file_txt='AUTH ಪ್ರಕಾರವನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಲಾಕ್ ಮಾಡಲಾಗಿದೆ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_LOCK_AUTH_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1170' AND lang_code='kan';
UPDATE master.template
SET "name"='Successful Locking of Auth Types Email Subject', descr='Successful Locking of Auth Types Email Subject', file_format_code='txt', model='velocity', file_txt='Successfully Locked the AUTH Type(s)', module_id='10006', module_name='Resident Services', template_typ_code='RS_LOCK_AUTH_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1170' AND lang_code='eng';
UPDATE master.template
SET "name"='Succès du verrouillage des types d''authentification', descr='Succès du verrouillage des types d''authentification', file_format_code='txt', model='velocity', file_txt='Le ou les types AUTH ont été verrouillés avec succès', module_id='10006', module_name='Resident Services', template_typ_code='RS_LOCK_AUTH_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1170' AND lang_code='fra';
UPDATE master.template
SET "name"='அங்கீகார வகைகளுக்கான மின்னஞ்சல் உள்ளடக்கத்தின் வெற்றிகரமான பூட்டு', descr='அங்கீகார வகைகளுக்கான மின்னஞ்சல் உள்ளடக்கத்தின் வெற்றிகரமான பூட்டு', file_format_code='txt', model='velocity', file_txt='VID இன் வெற்றிகரமான உருவாக்கம்', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_LOCK_AUTH_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1170' AND lang_code='tam';
UPDATE master.template
SET "name"='القفل الناجح لأنواع المصادقة SMS', descr='القفل الناجح لأنواع المصادقة SMS', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، تم تأمين نوع (أنواع) AUTH المطلوبة بنجاح. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_LOCK_AUTH_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1171' AND lang_code='ara';
UPDATE master.template
SET "name"='Verrouillage réussi des types d''authentification SMS', descr='Verrouillage réussi des types d''authentification SMS', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Le ou les types AUTH que vous avez demandés ont été verrouillés avec succès. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_LOCK_AUTH_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1171' AND lang_code='fra';
UPDATE master.template
SET "name"='Successful Locking of Auth Types SMS', descr='Successful Locking of Auth Types SMS', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your requested AUTH type(s) have been locked successfully. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_LOCK_AUTH_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1171' AND lang_code='eng';
UPDATE master.template
SET "name"='எஸ்எம்எஸ் அங்கீகார வகைகளின் வெற்றிகரமான பூட்டு', descr='எஸ்எம்எஸ் அங்கீகார வகைகளின் வெற்றிகரமான பூட்டு', file_format_code='txt', model='velocity', file_txt='ஹாய் $ fullName_eng, கோரப்பட்ட AUTH வகையை (களை) வெற்றிகரமாகப் பூட்டிவிட்டீர்கள். நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_LOCK_AUTH_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1171' AND lang_code='tam';
UPDATE master.template
SET "name"='प्रमाणीकरण प्रकार एसएमएस का सफल लॉक', descr='प्रमाणीकरण प्रकार एसएमएस का सफल लॉक', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, आपने अनुरोधित AUTH प्रकार (प्रकारों) को सफलतापूर्वक लॉक कर दिया है। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_LOCK_AUTH_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1171' AND lang_code='hin';
UPDATE master.template
SET "name"='ದೃಢೀಕರಣ ಪ್ರಕಾರಗಳ SMS ನ ಯಶಸ್ವಿ ಲಾಕ್', descr='ದೃಢೀಕರಣ ಪ್ರಕಾರಗಳ SMS ನ ಯಶಸ್ವಿ ಲಾಕ್', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ನೀವು ವಿನಂತಿಸಿದ AUTH ಪ್ರಕಾರ(ಗಳನ್ನು) ಯಶಸ್ವಿಯಾಗಿ ಲಾಕ್ ಮಾಡಲಾಗಿದೆ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_LOCK_AUTH_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1171' AND lang_code='kan';
UPDATE master.template
SET "name"='पुष्टिकरण प्रकार के ईमेल को सफलतापूर्वक अनलॉक करना', descr='पुष्टिकरण प्रकार के ईमेल को सफलतापूर्वक अनलॉक करना', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, आपके द्वारा अनुरोधित AUTH प्रकार (प्रकारों) को सफलतापूर्वक अनलॉक कर दिया गया है। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_UNLOCK_AUTH_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1172' AND lang_code='hin';
UPDATE master.template
SET "name"='فتح ناجح لأنواع البريد الإلكتروني للمصادقة', descr='فتح ناجح لأنواع البريد الإلكتروني للمصادقة', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، تم إلغاء تأمين نوع (أنواع) AUTH المطلوبة بنجاح. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_UNLOCK_AUTH_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1172' AND lang_code='ara';
UPDATE master.template
SET "name"='உறுதிப்படுத்தல் வகை மின்னஞ்சல் வெற்றிகரமாக திறக்கப்பட்டது', descr='உறுதிப்படுத்தல் வகை மின்னஞ்சல் வெற்றிகரமாக திறக்கப்பட்டது', file_format_code='txt', model='velocity', file_txt='ஹாய் $ fullName_eng, நீங்கள் கோரிய AUTH வகை (கள்) வெற்றிகரமாக திறக்கப்பட்டது. நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_UNLOCK_AUTH_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1172' AND lang_code='tam';
UPDATE master.template
SET "name"='Successful Unlocking of Auth Types Email', descr='Successful Unlocking of Auth Types Email', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your requested AUTH type(s) have been unlocked successfully. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UNLOCK_AUTH_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1172' AND lang_code='eng';
UPDATE master.template
SET "name"='Déverrouillage réussi des types d''authentification', descr='Déverrouillage réussi des types d''authentification', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Le ou les types AUTH demandés ont été déverrouillés avec succès. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UNLOCK_AUTH_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1172' AND lang_code='fra';
UPDATE master.template
SET "name"='ದೃಢೀಕರಣ ಪ್ರಕಾರದ ಇಮೇಲ್‌ನ ಯಶಸ್ವಿ ಅನ್‌ಲಾಕಿಂಗ್', descr='ದೃಢೀಕರಣ ಪ್ರಕಾರದ ಇಮೇಲ್‌ನ ಯಶಸ್ವಿ ಅನ್‌ಲಾಕಿಂಗ್', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ನೀವು ವಿನಂತಿಸಿದ AUTH ಪ್ರಕಾರ(ಗಳನ್ನು) ಯಶಸ್ವಿಯಾಗಿ ಅನ್‌ಲಾಕ್ ಮಾಡಲಾಗಿದೆ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_UNLOCK_AUTH_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1172' AND lang_code='kan';
UPDATE master.template
SET "name"='Successful Unlocking of Auth Types Email Subject', descr='Successful Unlocking of Auth Types Email Subject', file_format_code='txt', model='velocity', file_txt='Successfully Unlocked the AUTH Type(s)', module_id='10006', module_name='Resident Services', template_typ_code='RS_UNLOCK_AUTH_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1173' AND lang_code='eng';
UPDATE master.template
SET "name"='Déverrouillage réussi des types d''authentification Objet de l''e-mail', descr='Déverrouillage réussi des types d''authentification Objet de l''e-mail', file_format_code='txt', model='velocity', file_txt='Déverrouillé avec succès le (s) type (s) AUTH', module_id='10006', module_name='Resident Services', template_typ_code='RS_UNLOCK_AUTH_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1173' AND lang_code='fra';
UPDATE master.template
SET "name"='فتح ناجح لأنواع المصادقة موضوع البريد الإلكتروني', descr='فتح ناجح لأنواع المصادقة موضوع البريد الإلكتروني', file_format_code='txt', model='velocity', file_txt='تم فتح نوع (أنواع) AUTH بنجاح', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_UNLOCK_AUTH_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1173' AND lang_code='ara';
UPDATE master.template
SET "name"='ದೃಢೀಕರಣ ಪ್ರಕಾರಗಳ ಇಮೇಲ್ ವಿಷಯದ ಯಶಸ್ವಿ ಅನ್‌ಲಾಕಿಂಗ್', descr='ದೃಢೀಕರಣ ಪ್ರಕಾರಗಳ ಇಮೇಲ್ ವಿಷಯದ ಯಶಸ್ವಿ ಅನ್‌ಲಾಕಿಂಗ್', file_format_code='txt', model='velocity', file_txt='AUTH ಪ್ರಕಾರ(ಗಳನ್ನು) ಯಶಸ್ವಿಯಾಗಿ ಅನ್‌ಲಾಕ್ ಮಾಡಲಾಗಿದೆ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_UNLOCK_AUTH_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1173' AND lang_code='kan';
UPDATE master.template
SET "name"='प्रमाणीकरण प्रकारों के लिए ईमेल सामग्री को सफलतापूर्वक अनलॉक करना', descr='प्रमाणीकरण प्रकारों के लिए ईमेल सामग्री को सफलतापूर्वक अनलॉक करना', file_format_code='txt', model='velocity', file_txt='AUTH प्रकार सफलतापूर्वक अनलॉक किए गए', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_UNLOCK_AUTH_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1173' AND lang_code='hin';
UPDATE master.template
SET "name"='அங்கீகார வகைகளுக்கான மின்னஞ்சல் உள்ளடக்கம் வெற்றிகரமாக திறக்கப்பட்டது', descr='அங்கீகார வகைகளுக்கான மின்னஞ்சல் உள்ளடக்கம் வெற்றிகரமாக திறக்கப்பட்டது', file_format_code='txt', model='velocity', file_txt='AUTH வகை (கள்) வெற்றிகரமாக திறக்கப்பட்டது', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_UNLOCK_AUTH_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1173' AND lang_code='tam';
UPDATE master.template
SET "name"='Déverrouillage réussi des types d''authentification SMS', descr='Déverrouillage réussi des types d''authentification SMS', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Le ou les types AUTH demandés ont été déverrouillés avec succès. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UNLOCK_AUTH_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1174' AND lang_code='fra';
UPDATE master.template
SET "name"='ದೃಢೀಕರಣ ಪ್ರಕಾರಗಳ ಎಸ್‌ಎಂಎಸ್‌ನ ಯಶಸ್ವಿ ಅನ್‌ಲಾಕಿಂಗ್', descr='ದೃಢೀಕರಣ ಪ್ರಕಾರಗಳ ಎಸ್‌ಎಂಎಸ್‌ನ ಯಶಸ್ವಿ ಅನ್‌ಲಾಕಿಂಗ್', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ನೀವು ವಿನಂತಿಸಿದ AUTH ಪ್ರಕಾರ(ಗಳನ್ನು) ಯಶಸ್ವಿಯಾಗಿ ಅನ್‌ಲಾಕ್ ಮಾಡಲಾಗಿದೆ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_UNLOCK_AUTH_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1174' AND lang_code='kan';
UPDATE master.template
SET "name"='प्रमाणीकरण प्रकार एसएमएस का सफल अनलॉकिंग', descr='प्रमाणीकरण प्रकार एसएमएस का सफल अनलॉकिंग', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, आपके द्वारा अनुरोधित AUTH प्रकार (प्रकारों) को सफलतापूर्वक अनलॉक कर दिया गया है। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_UNLOCK_AUTH_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1174' AND lang_code='hin';
UPDATE master.template
SET "name"='அங்கீகார வகை எஸ்எம்எஸ் வெற்றிகரமாக திறக்கப்பட்டது', descr='அங்கீகார வகை எஸ்எம்எஸ் வெற்றிகரமாக திறக்கப்பட்டது', file_format_code='txt', model='velocity', file_txt='ஹாய் $ fullName_eng, நீங்கள் கோரிய AUTH வகை (கள்) வெற்றிகரமாக திறக்கப்பட்டது. நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_UNLOCK_AUTH_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1174' AND lang_code='tam';
UPDATE master.template
SET "name"='Successful Unlocking of Auth Types SMS', descr='Successful Unlocking of Auth Types SMS', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your requested AUTH type(s) have been unlocked successfully. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UNLOCK_AUTH_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1174' AND lang_code='eng';
UPDATE master.template
SET "name"='فتح ناجح لأنواع مصادقة الرسائل القصيرة', descr='فتح ناجح لأنواع مصادقة الرسائل القصيرة', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، تم إلغاء تأمين نوع (أنواع) AUTH المطلوبة بنجاح. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_UNLOCK_AUTH_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1174' AND lang_code='ara';
UPDATE master.template
SET "name"='VID ಜನರೇಷನ್ ಯಶಸ್ಸಿನ ಇಮೇಲ್', descr='VID ಜನರೇಷನ್ ಯಶಸ್ಸಿನ ಇಮೇಲ್', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ನಿಮ್ಮ UIN ಗಾಗಿ ನಿಮ್ಮ VID ಅನ್ನು ಯಶಸ್ವಿಯಾಗಿ ರಚಿಸಲಾಗಿದೆ. ನಿಮ್ಮ VID ಸಂಖ್ಯೆ $VID ಆಗಿದೆ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_VIN_GEN_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1175' AND lang_code='kan';
UPDATE master.template
SET "name"='VID Generation Success Email', descr='VID Generation Success Email', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your VID for your UIN has been successfully generated. Your VID number is $VID. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_GEN_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1175' AND lang_code='eng';
UPDATE master.template
SET "name"='E-mail de réussite de la génération VID', descr='E-mail de réussite de la génération VID', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre VID pour votre UIN a été généré avec succès. Votre numéro VID est $VID. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_GEN_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1175' AND lang_code='fra';
UPDATE master.template
SET "name"='البريد الإلكتروني لنجاح إنشاء VID', descr='البريد الإلكتروني لنجاح إنشاء VID', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، تم إنشاء VID الخاص بك لـ UIN الخاص بك بنجاح. رقم VID الخاص بك هو $ VID. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_VIN_GEN_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1175' AND lang_code='ara';
UPDATE master.template
SET "name"='VID தலைமுறை வெற்றி மின்னஞ்சல்', descr='VID தலைமுறை வெற்றி மின்னஞ்சல்', file_format_code='txt', model='velocity', file_txt='வணக்கம் $ fullName_eng, உங்கள் UIN க்காக உங்கள் VID ஐ வெற்றிகரமாக உருவாக்கியது. உங்கள் VID எண் $ VID ஆகும். நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_VIN_GEN_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1175' AND lang_code='tam';
UPDATE master.template
SET "name"='VID जनरेशन सक्सेस ईमेल', descr='VID जनरेशन सक्सेस ईमेल', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, आपके UIN के लिए सफलतापूर्वक आपका VID बना लिया है। आपका VID नंबर $ VID है। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_VIN_GEN_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1175' AND lang_code='hin';
UPDATE master.template
SET "name"='वीआईडी जनरेशन सक्सेस ईमेल कंटेंट', descr='वीआईडी जनरेशन सक्सेस ईमेल कंटेंट', file_format_code='txt', model='velocity', file_txt=' VID . की सफल पीढ़ी', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_VIN_GEN_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1176' AND lang_code='hin';
UPDATE master.template
SET "name"='VID Generation Success Email Subject', descr='VID Generation Success Email Subject', file_format_code='txt', model='velocity', file_txt='Successful Generation of VID', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_GEN_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1176' AND lang_code='eng';
UPDATE master.template
SET "name"='Sujet de l''e-mail de réussite de la génération VID', descr='Sujet de l''e-mail de réussite de la génération VID', file_format_code='txt', model='velocity', file_txt='Génération réussie de VID', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_GEN_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1176' AND lang_code='fra';
UPDATE master.template
SET "name"='VID தலைமுறை வெற்றி மின்னஞ்சல் உள்ளடக்கம்', descr='VID தலைமுறை வெற்றி மின்னஞ்சல் உள்ளடக்கம்', file_format_code='txt', model='velocity', file_txt='VID இன் வெற்றிகரமான உருவாக்கம்', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_VIN_GEN_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1176' AND lang_code='tam';
UPDATE master.template
SET "name"='VID ಜನರೇಷನ್ ಯಶಸ್ಸಿನ ಇಮೇಲ್ ವಿಷಯ', descr='VID ಜನರೇಷನ್ ಯಶಸ್ಸಿನ ಇಮೇಲ್ ವಿಷಯ', file_format_code='txt', model='velocity', file_txt='VID ಯ ಯಶಸ್ವಿ ಪೀಳಿಗೆ ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_VIN_GEN_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1176' AND lang_code='kan';
UPDATE master.template
SET "name"='موضوع البريد الإلكتروني لنجاح إنشاء VID', descr='موضوع البريد الإلكتروني لنجاح إنشاء VID', file_format_code='txt', model='velocity', file_txt='الجيل الناجح من VID', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_VIN_GEN_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1176' AND lang_code='ara';
UPDATE master.template
SET "name"='VID Generation Success SMS', descr='VID Generation Success SMS', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your VID for your UIN has been successfully generated. Your VID number is $VID. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_GEN_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1177' AND lang_code='eng';
UPDATE master.template
SET "name"='सफलता के लिए वीआईडी जनरेशन एसएमएस', descr='सफलता के लिए वीआईडी जनरेशन एसएमएस', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, आपके UIN के लिए सफलतापूर्वक आपका VID बना लिया है। आपका VID नंबर $ VID है। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_VIN_GEN_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1177' AND lang_code='hin';
UPDATE master.template
SET "name"='வெற்றிக்கான விஐடி தலைமுறை எஸ்எம்எஸ்', descr='வெற்றிக்கான விஐடி தலைமுறை எஸ்எம்எஸ்', file_format_code='txt', model='velocity', file_txt='வணக்கம் $ fullName_eng, உங்கள் UIN க்காக உங்கள் VID ஐ வெற்றிகரமாக உருவாக்கியது. உங்கள் VID எண் $ VID ஆகும். நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_VIN_GEN_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1177' AND lang_code='tam';
UPDATE master.template
SET "name"='نجاح إنشاء VID SMS', descr='نجاح إنشاء VID SMS', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، تم إنشاء VID الخاص بك لـ UIN الخاص بك بنجاح. رقم VID الخاص بك هو $ VID. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_VIN_GEN_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1177' AND lang_code='ara';
UPDATE master.template
SET "name"='VID ಜನರೇಷನ್ ಯಶಸ್ಸಿನ SMS', descr='VID ಜನರೇಷನ್ ಯಶಸ್ಸಿನ SMS', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ನಿಮ್ಮ UIN ಗಾಗಿ ನಿಮ್ಮ VID ಅನ್ನು ಯಶಸ್ವಿಯಾಗಿ ರಚಿಸಲಾಗಿದೆ. ನಿಮ್ಮ VID ಸಂಖ್ಯೆ $VID ಆಗಿದೆ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_VIN_GEN_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1177' AND lang_code='kan';
UPDATE master.template
SET "name"='SMS de réussite de génération VID', descr='SMS de réussite de génération VID', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre VID pour votre UIN a été généré avec succès. Votre numéro VID est $VID. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_GEN_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1177' AND lang_code='fra';
UPDATE master.template
SET "name"='VID निकासी सफल ईमेल', descr='VID निकासी सफल ईमेल', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, आपका $ VID सफलतापूर्वक पुनर्प्राप्त कर लिया गया है। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_VIN_REV_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1178' AND lang_code='hin';
UPDATE master.template
SET "name"='البريد الإلكتروني لنجاح إبطال VID', descr='البريد الإلكتروني لنجاح إبطال VID', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، تم إبطال $ VID الخاص بك بنجاح. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_VIN_REV_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1178' AND lang_code='ara';
UPDATE master.template
SET "name"='E-mail de réussite de la révocation de VID', descr='E-mail de réussite de la révocation de VID', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre $VID a bien été révoqué. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_REV_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1178' AND lang_code='fra';
UPDATE master.template
SET "name"='VID Revocation Success Email', descr='VID Revocation Success Email', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your $VID has been successfully revoked. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_REV_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1178' AND lang_code='eng';
UPDATE master.template
SET "name"='VID ಹಿಂತೆಗೆದುಕೊಳ್ಳುವಿಕೆ ಯಶಸ್ವಿ ಇಮೇಲ್', descr='VID ಹಿಂತೆಗೆದುಕೊಳ್ಳುವಿಕೆ ಯಶಸ್ವಿ ಇಮೇಲ್', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ನಿಮ್ಮ $VID ಅನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಹಿಂಪಡೆಯಲಾಗಿದೆ. ಧನ್ಯವಾದಗಳು! ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_VIN_REV_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1178' AND lang_code='kan';
UPDATE master.template
SET "name"='VID திரும்பப் பெறுதல் வெற்றிகரமான மின்னஞ்சல்', descr='VID திரும்பப் பெறுதல் வெற்றிகரமான மின்னஞ்சல்', file_format_code='txt', model='velocity', file_txt='வணக்கம் $ fullName_eng, உங்கள் $ VID வெற்றிகரமாக மீட்டெடுக்கப்பட்டது. நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_VIN_REV_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1178' AND lang_code='tam';
UPDATE master.template
SET "name"='VID ಹಿಂತೆಗೆದುಕೊಳ್ಳುವಿಕೆಯ ಯಶಸ್ಸಿನ ಇಮೇಲ್ ವಿಷಯ', descr='VID ಹಿಂತೆಗೆದುಕೊಳ್ಳುವಿಕೆಯ ಯಶಸ್ಸಿನ ಇಮೇಲ್ ವಿಷಯ', file_format_code='txt', model='velocity', file_txt='VID ಯ ಯಶಸ್ವಿ ಹಿಂಪಡೆಯುವಿಕೆ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_VIN_REV_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1179' AND lang_code='kan';
UPDATE master.template
SET "name"='موضوع البريد الإلكتروني لنجاح إبطال VID', descr='موضوع البريد الإلكتروني لنجاح إبطال VID', file_format_code='txt', model='velocity', file_txt='الإلغاء الناجح لـ VID', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_VIN_REV_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1179' AND lang_code='ara';
UPDATE master.template
SET "name"='Sujet de l''e-mail de réussite de la révocation de VID', descr='Sujet de l''e-mail de réussite de la révocation de VID', file_format_code='txt', model='velocity', file_txt='Révocation réussie de VID', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_REV_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1179' AND lang_code='fra';
UPDATE master.template
SET "name"='VID Revocation Success Email Subject', descr='VID Revocation Success Email Subject', file_format_code='txt', model='velocity', file_txt='Successful Revocation of VID', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_REV_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1179' AND lang_code='eng';
UPDATE master.template
SET "name"='VID निकासी सफलता ईमेल सामग्री', descr='VID निकासी सफलता ईमेल सामग्री', file_format_code='txt', model='velocity', file_txt=' VID . की सफल पुनर्प्राप्ति', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_VIN_REV_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1179' AND lang_code='hin';
UPDATE master.template
SET "name"='VID திரும்பப் பெறுதல் வெற்றி மின்னஞ்சல் உள்ளடக்கம்', descr='VID திரும்பப் பெறுதல் வெற்றி மின்னஞ்சல் உள்ளடக்கம்', file_format_code='txt', model='velocity', file_txt='VID இன் வெற்றிகரமான மீட்டெடுப்பு', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_VIN_REV_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1179' AND lang_code='tam';
UPDATE master.template
SET "name"='SMS de succès de révocation VID', descr='SMS de succès de révocation VID', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre $VID a bien été révoqué. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_REV_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1180' AND lang_code='fra';
UPDATE master.template
SET "name"='SMS نجاح إبطال VID', descr='SMS نجاح إبطال VID', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، تم إبطال $ VID الخاص بك بنجاح. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_VIN_REV_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1180' AND lang_code='ara';
UPDATE master.template
SET "name"='VID ಹಿಂತೆಗೆದುಕೊಳ್ಳುವಿಕೆಯ ಯಶಸ್ಸಿನ SMS', descr='VID ಹಿಂತೆಗೆದುಕೊಳ್ಳುವಿಕೆಯ ಯಶಸ್ಸಿನ SMS', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ನಿಮ್ಮ $VID ಅನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಹಿಂಪಡೆಯಲಾಗಿದೆ. ಧನ್ಯವಾದಗಳು! ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_VIN_REV_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1180' AND lang_code='kan';
UPDATE master.template
SET "name"='वीआईडी निकासी सफलता एसएमएस', descr='वीआईडी निकासी सफलता एसएमएस', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, आपका $ VID सफलतापूर्वक पुनर्प्राप्त कर लिया गया है। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_VIN_REV_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1180' AND lang_code='hin';
UPDATE master.template
SET "name"='VID Revocation Success SMS', descr='VID Revocation Success SMS', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your $VID has been successfully revoked. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_REV_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1180' AND lang_code='eng';
UPDATE master.template
SET "name"='VID திரும்பப் பெறுதல் வெற்றி SMS', descr='VID திரும்பப் பெறுதல் வெற்றி SMS', file_format_code='txt', model='velocity', file_txt='வணக்கம் $ fullName_eng, உங்கள் $ VID வெற்றிகரமாக மீட்டெடுக்கப்பட்டது. நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_VIN_REV_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1180' AND lang_code='tam';
UPDATE master.template
SET "name"='Réimpression de l''e-mail de réussite de la demande', descr='Réimpression de l''e-mail de réussite de la demande', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre demande de "Réimpression d''UIN" a été placée avec succès. Votre RID (numéro de demande) est $RID. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_RPR_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1181' AND lang_code='fra';
UPDATE master.template
SET "name"='إعادة طباعة طلب البريد الإلكتروني بنجاح', descr='إعادة طباعة طلب البريد الإلكتروني بنجاح', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، تم تقديم طلبك لـ "إعادة طباعة UIN" بنجاح. RID الخاص بك (رقم الطلب) هو $ RID. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_UIN_RPR_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1181' AND lang_code='ara';
UPDATE master.template
SET "name"='Reprint Request Success Email', descr='Reprint Request Success Email', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your request for "Reprint Of UIN" has been successfully placed. Your RID (Req Number) is $RID. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_RPR_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1181' AND lang_code='eng';
UPDATE master.template
SET "name"='ಮರುಮುದ್ರಣ ವಿನಂತಿಯ ಯಶಸ್ವಿ ಇಮೇಲ್', descr='ಮರುಮುದ್ರಣ ವಿನಂತಿಯ ಯಶಸ್ವಿ ಇಮೇಲ್', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, "UIN ಮರುಮುದ್ರಣ" ಗಾಗಿ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಇರಿಸಲಾಗಿದೆ. ನಿಮ್ಮ RID (Req ಸಂಖ್ಯೆ) $RID ಆಗಿದೆ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_UIN_RPR_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1181' AND lang_code='kan';
UPDATE master.template
SET "name"='மறுபதிப்பு கோரிக்கையின் வெற்றிகரமான மின்னஞ்சல்', descr='மறுபதிப்பு கோரிக்கையின் வெற்றிகரமான மின்னஞ்சல்', file_format_code='txt', model='velocity', file_txt='ஹாய் $ fullName_eng, "UIN மறுபதிப்பு"க்கான உங்கள் கோரிக்கை வெற்றிகரமாக வைக்கப்பட்டது. உங்கள் RID (Req Number) $ RID ஆகும். நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_UIN_RPR_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1181' AND lang_code='tam';
UPDATE master.template
SET "name"='पुनर्मुद्रण अनुरोध का सफल ईमेल', descr='पुनर्मुद्रण अनुरोध का सफल ईमेल', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, "यूआईएन पुनर्मुद्रण" के लिए आपका अनुरोध सफलतापूर्वक कर दिया गया है। आपका RID (Req Number) $ RID है। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_UIN_RPR_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1181' AND lang_code='hin';
UPDATE master.template
SET "name"='पुनर्मुद्रण अनुरोध सफलता के लिए ईमेल सामग्री', descr='पुनर्मुद्रण अनुरोध सफलता के लिए ईमेल सामग्री', file_format_code='txt', model='velocity', file_txt='पुनर्मुद्रण अनुरोध सफल रहा', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_UIN_RPR_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1182' AND lang_code='hin';
UPDATE master.template
SET "name"='ಮರುಮುದ್ರಣ ವಿನಂತಿಯ ಯಶಸ್ಸಿನ ಇಮೇಲ್ ವಿಷಯ', descr='ಮರುಮುದ್ರಣ ವಿನಂತಿಯ ಯಶಸ್ಸಿನ ಇಮೇಲ್ ವಿಷಯ', file_format_code='txt', model='velocity', file_txt='ಮರುಮುದ್ರಣ ವಿನಂತಿ ಯಶಸ್ವಿಯಾಗಿದೆ ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_UIN_RPR_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1182' AND lang_code='kan';
UPDATE master.template
SET "name"='மறுபதிப்பு கோரிக்கை வெற்றிக்கான மின்னஞ்சல் உள்ளடக்கம்', descr='மறுபதிப்பு கோரிக்கை வெற்றிக்கான மின்னஞ்சல் உள்ளடக்கம்', file_format_code='txt', model='velocity', file_txt='மறுபதிப்பு கோரிக்கை வெற்றியடைந்தது', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_UIN_RPR_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1182' AND lang_code='tam';
UPDATE master.template
SET "name"='Reprint Request Success Email Subject', descr='Reprint Request Success Email Subject', file_format_code='txt', model='velocity', file_txt='Reprint Request Successful', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_RPR_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1182' AND lang_code='eng';
UPDATE master.template
SET "name"='Sujet de l''e-mail de demande de réimpression réussie', descr='Sujet de l''e-mail de demande de réimpression réussie', file_format_code='txt', model='velocity', file_txt='Réimpression de la demande réussie', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_RPR_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1182' AND lang_code='fra';
UPDATE master.template
SET "name"='إعادة طباعة عنوان البريد الإلكتروني بنجاح', descr='إعادة طباعة عنوان البريد الإلكتروني بنجاح', file_format_code='txt', model='velocity', file_txt='تم طلب إعادة الطباعة بنجاح', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_UIN_RPR_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1182' AND lang_code='ara';
UPDATE master.template
SET "name"='पुनर्मुद्रण अनुरोध सफलता एसएमएस', descr='पुनर्मुद्रण अनुरोध सफलता एसएमएस', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, "यूआईएन पुनर्मुद्रण" के लिए आपका अनुरोध सफलतापूर्वक कर दिया गया है। आपका RID (Req Number) $ RID है। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_UIN_RPR_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1183' AND lang_code='hin';
UPDATE master.template
SET "name"='Reprint Request Success SMS', descr='Reprint Request Success SMS', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your request for "Reprint Of UIN" has been successfully placed. Your RID (Req Number) is $RID. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_RPR_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1183' AND lang_code='eng';
UPDATE master.template
SET "name"='Réimpression SMS de réussite de la demande', descr='Réimpression SMS de réussite de la demande', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre demande de "Réimpression d''UIN" a été placée avec succès. Votre RID (numéro de demande) est $RID. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_RPR_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1183' AND lang_code='fra';
UPDATE master.template
SET "name"='إعادة طباعة طلب النجاح SMS', descr='إعادة طباعة طلب النجاح SMS', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، تم تقديم طلبك لـ "إعادة طباعة UIN" بنجاح. RID الخاص بك (رقم الطلب) هو $ RID. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_UIN_RPR_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1183' AND lang_code='ara';
UPDATE master.template
SET "name"='மறுபதிப்பு கோரிக்கை வெற்றி SMS', descr='மறுபதிப்பு கோரிக்கை வெற்றி SMS', file_format_code='txt', model='velocity', file_txt='ஹாய் $ fullName_eng, "UIN மறுபதிப்பு"க்கான உங்கள் கோரிக்கை வெற்றிகரமாக வைக்கப்பட்டது. உங்கள் RID (Req Number) $ RID ஆகும். நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_UIN_RPR_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1183' AND lang_code='tam';
UPDATE master.template
SET "name"='ಮರುಮುದ್ರಣ ವಿನಂತಿ ಯಶಸ್ಸಿನ SMS', descr='ಮರುಮುದ್ರಣ ವಿನಂತಿ ಯಶಸ್ಸಿನ SMS', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, "UIN ಮರುಮುದ್ರಣ" ಗಾಗಿ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಇರಿಸಲಾಗಿದೆ. ನಿಮ್ಮ RID (Req ಸಂಖ್ಯೆ) $RID ಆಗಿದೆ. ಧನ್ಯವಾದಗಳು! ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_UIN_RPR_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1183' AND lang_code='kan';
UPDATE master.template
SET "name"='உறுதிப்படுத்தல் வரலாறு கோரிக்கை மின்னஞ்சல் தோல்வி', descr='உறுதிப்படுத்தல் வரலாறு கோரிக்கை மின்னஞ்சல் தோல்வி', file_format_code='txt', model='velocity', file_txt='ஹாய் $ fullName_eng, உங்கள் உறுதிப்படுத்தல் வரலாறு விவரங்களைப் பெறுவதற்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_AUTH_HIST_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1184' AND lang_code='tam';
UPDATE master.template
SET "name"='ದೃಢೀಕರಣ ಇತಿಹಾಸ ವಿನಂತಿ ವಿಫಲ ಇಮೇಲ್', descr='ದೃಢೀಕರಣ ಇತಿಹಾಸ ವಿನಂತಿ ವಿಫಲ ಇಮೇಲ್', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ನಿಮ್ಮ ದೃಢೀಕರಣ ಇತಿಹಾಸದ ವಿವರಗಳನ್ನು ಪಡೆಯುವ ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. ಧನ್ಯವಾದಗಳು! ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_AUTH_HIST_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1184' AND lang_code='kan';
UPDATE master.template
SET "name"='E-mail d''échec de la demande d''historique d''authentification', descr='E-mail d''échec de la demande d''historique d''authentification', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre demande de récupération des détails de votre historique d''authentification a échoué. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_AUTH_HIST_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1184' AND lang_code='fra';
UPDATE master.template
SET "name"='تاريخ المصادقة طلب فشل البريد الإلكتروني', descr='تاريخ المصادقة طلب فشل البريد الإلكتروني', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، لقد فشل طلبك لجلب تفاصيل سجل المصادقة. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_AUTH_HIST_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1184' AND lang_code='ara';
UPDATE master.template
SET "name"='पुष्टिकरण इतिहास अनुरोध विफल ईमेल', descr='पुष्टिकरण इतिहास अनुरोध विफल ईमेल', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, आपका पुष्टिकरण इतिहास विवरण प्राप्त करने का आपका अनुरोध विफल हो गया। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_AUTH_HIST_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1184' AND lang_code='hin';
UPDATE master.template
SET "name"='Authentication History Request Failure Email', descr='Authentication History Request Failure Email', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your Request to fetch your Authentication History details has failed. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_AUTH_HIST_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1184' AND lang_code='eng';
UPDATE master.template
SET "name"='அங்கீகார வரலாறு கோரிக்கை தோல்வியுற்ற மின்னஞ்சல் உள்ளடக்கம்', descr='அங்கீகார வரலாறு கோரிக்கை தோல்வியுற்ற மின்னஞ்சல் உள்ளடக்கம்', file_format_code='txt', model='velocity', file_txt='அங்கீகார வரலாறு கோரிக்கை தோல்வியடைந்தது', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_AUTH_HIST_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1185' AND lang_code='tam';
UPDATE master.template
SET "name"='प्रमाणीकरण इतिहास अनुरोध विफल ईमेल सामग्री', descr='प्रमाणीकरण इतिहास अनुरोध विफल ईमेल सामग्री', file_format_code='txt', model='velocity', file_txt='प्रमाणीकरण इतिहास अनुरोध विफल रहा', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_AUTH_HIST_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1185' AND lang_code='hin';
UPDATE master.template
SET "name"='ದೃಢೀಕರಣ ಇತಿಹಾಸ ವಿನಂತಿ ವಿಫಲ ಇಮೇಲ್ ವಿಷಯ', descr='ದೃಢೀಕರಣ ಇತಿಹಾಸ ವಿನಂತಿ ವಿಫಲ ಇಮೇಲ್ ವಿಷಯ', file_format_code='txt', model='velocity', file_txt='ದೃಢೀಕರಣ ಇತಿಹಾಸ ವಿನಂತಿ ವಿಫಲವಾಗಿದೆ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_AUTH_HIST_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1185' AND lang_code='kan';
UPDATE master.template
SET "name"='Authentication History Request Failure Email Subject', descr='Authentication History Request Failure Email Subject', file_format_code='txt', model='velocity', file_txt='Authentication History Request Failure', module_id='10006', module_name='Resident Services', template_typ_code='RS_AUTH_HIST_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1185' AND lang_code='eng';
UPDATE master.template
SET "name"='Objet de l''e-mail d''échec de la demande d''historique d''authentification', descr='Objet de l''e-mail d''échec de la demande d''historique d''authentification', file_format_code='txt', model='velocity', file_txt='Échec de la demande d''historique d''authentification', module_id='10006', module_name='Resident Services', template_typ_code='RS_AUTH_HIST_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1185' AND lang_code='fra';
UPDATE master.template
SET "name"='فشل طلب محفوظات المصادقة عبر البريد الإلكتروني', descr='فشل طلب محفوظات المصادقة عبر البريد الإلكتروني', file_format_code='txt', model='velocity', file_txt='فشل طلب محفوظات المصادقة', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_AUTH_HIST_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1185' AND lang_code='ara';
UPDATE master.template
SET "name"='प्रमाणीकरण इतिहास अनुरोध विफल एसएमएस', descr='प्रमाणीकरण इतिहास अनुरोध विफल एसएमएस', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, प्रमाणीकरण इतिहास के लिए आपका अनुरोध विफल रहा। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_AUTH_HIST_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1186' AND lang_code='hin';
UPDATE master.template
SET "name"='Authentication History Request Failure SMS', descr='Authentication History Request Failure SMS', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your Request for Authentication History has failed. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_AUTH_HIST_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1186' AND lang_code='eng';
UPDATE master.template
SET "name"='SMS d''échec de la demande d''historique d''authentification', descr='SMS d''échec de la demande d''historique d''authentification', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre demande de récupération des détails de votre historique d''authentification a échoué. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_AUTH_HIST_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1186' AND lang_code='fra';
UPDATE master.template
SET "name"='فشل طلب محفوظات المصادقة SMS', descr='فشل طلب محفوظات المصادقة SMS', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، لقد فشل طلبك لسجل المصادقة. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_AUTH_HIST_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1186' AND lang_code='ara';
UPDATE master.template
SET "name"='ದೃಢೀಕರಣ ಇತಿಹಾಸ ವಿನಂತಿ ವಿಫಲವಾದ SMS', descr='ದೃಢೀಕರಣ ಇತಿಹಾಸ ವಿನಂತಿ ವಿಫಲವಾದ SMS', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ದೃಢೀಕರಣ ಇತಿಹಾಸಕ್ಕಾಗಿ ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_AUTH_HIST_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1186' AND lang_code='kan';
UPDATE master.template
SET "name"='அங்கீகார வரலாறு கோரிக்கை எஸ்எம்எஸ் தோல்வியடைந்தது', descr='அங்கீகார வரலாறு கோரிக்கை எஸ்எம்எஸ் தோல்வியடைந்தது', file_format_code='txt', model='velocity', file_txt='வணக்கம் $ fullName_eng, அங்கீகார வரலாற்றிற்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_AUTH_HIST_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1186' AND lang_code='tam';
UPDATE master.template
SET "name"='Télécharger l''e-mail d''échec e-UIN', descr='Télécharger l''e-mail d''échec e-UIN', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre demande de téléchargement de votre e-UIN a échoué. Veuillez réessayer plus tard. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_DOW_UIN_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1187' AND lang_code='fra';
UPDATE master.template
SET "name"='ई-यूआईएन विफलता ईमेल डाउनलोड करें', descr='ई-यूआईएन विफलता ईमेल डाउनलोड करें', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, अपना ई-यूआईएन डाउनलोड करें! लोड करने का आपका अनुरोध विफल रहा। बाद में पुन: प्रयास करें। शुक्रिया', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_DOW_UIN_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1187' AND lang_code='hin';
UPDATE master.template
SET "name"='ಇ-ಯುಐಎನ್ ವೈಫಲ್ಯ ಇಮೇಲ್ ಅನ್ನು ಡೌನ್‌ಲೋಡ್ ಮಾಡಿ', descr='ಇ-ಯುಐಎನ್ ವೈಫಲ್ಯ ಇಮೇಲ್ ಅನ್ನು ಡೌನ್‌ಲೋಡ್ ಮಾಡಿ', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ನಿಮ್ಮ e-UIN ಅನ್ನು ಡೌನ್‌ಲೋಡ್ ಮಾಡಲು ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. ದಯವಿಟ್ಟು ನಂತರ ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ. ಧನ್ಯವಾದಗಳು! ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_DOW_UIN_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1187' AND lang_code='kan';
UPDATE master.template
SET "name"='E-UIN தோல்வி மின்னஞ்சலைப் பதிவிறக்கவும்', descr='E-UIN தோல்வி மின்னஞ்சலைப் பதிவிறக்கவும்', file_format_code='txt', model='velocity', file_txt='வணக்கம் $ fullName_eng, உங்கள் e-UIN ஐப் பதிவிறக்குவதற்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. பிறகு முயற்சிக்கவும். நன்றி! ', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_DOW_UIN_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1187' AND lang_code='tam';
UPDATE master.template
SET "name"='Download e-UIN Failure Email', descr='Download e-UIN Failure Email', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your request for downloading your e-UIN has failed. Please try again later. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_DOW_UIN_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1187' AND lang_code='eng';
UPDATE master.template
SET "name"='تنزيل البريد الإلكتروني لفشل e-UIN', descr='تنزيل البريد الإلكتروني لفشل e-UIN', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، لقد فشل طلبك لتنزيل e-UIN الخاص بك. الرجاء معاودة المحاولة في وقت لاحق. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_DOW_UIN_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1187' AND lang_code='ara';
UPDATE master.template
SET "name"='Télécharger le sujet de l''e-mail d''échec e-UIN', descr='Télécharger le sujet de l''e-mail d''échec e-UIN', file_format_code='txt', model='velocity', file_txt='Échec du téléchargement de l''e-UIN', module_id='10006', module_name='Resident Services', template_typ_code='RS_DOW_UIN_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1188' AND lang_code='fra';
UPDATE master.template
SET "name"='تنزيل موضوع البريد الإلكتروني لفشل e-UIN', descr='تنزيل موضوع البريد الإلكتروني لفشل e-UIN', file_format_code='txt', model='velocity', file_txt='فشل تنزيل e-UIN', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_DOW_UIN_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1188' AND lang_code='ara';
UPDATE master.template
SET "name"='ई-यूआईएन विफलता ईमेल सामग्री डाउनलोड करें', descr='ई-यूआईएन विफलता ईमेल सामग्री डाउनलोड करें', file_format_code='txt', model='velocity', file_txt='ई-यूआईएन डाउनलोड विफलता', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_DOW_UIN_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1188' AND lang_code='hin';
UPDATE master.template
SET "name"='Download e-UIN Failure Email Subject', descr='Download e-UIN Failure Email Subject', file_format_code='txt', model='velocity', file_txt='Download e-UIN Failure', module_id='10006', module_name='Resident Services', template_typ_code='RS_DOW_UIN_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1188' AND lang_code='eng';
UPDATE master.template
SET "name"='ಇ-ಯುಐಎನ್ ವೈಫಲ್ಯ ಇಮೇಲ್ ವಿಷಯವನ್ನು ಡೌನ್‌ಲೋಡ್ ಮಾಡಿ', descr='ಇ-ಯುಐಎನ್ ವೈಫಲ್ಯ ಇಮೇಲ್ ವಿಷಯವನ್ನು ಡೌನ್‌ಲೋಡ್ ಮಾಡಿ', file_format_code='txt', model='velocity', file_txt='ಇ-ಯುಐಎನ್ ಡೌನ್‌ಲೋಡ್ ವಿಫಲತೆ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_DOW_UIN_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1188' AND lang_code='kan';
UPDATE master.template
SET "name"='E-UIN தோல்வி மின்னஞ்சல் உள்ளடக்கத்தைப் பதிவிறக்கவும்', descr='E-UIN தோல்வி மின்னஞ்சல் உள்ளடக்கத்தைப் பதிவிறக்கவும்', file_format_code='txt', model='velocity', file_txt='E-UIN பதிவிறக்கம் தோல்வி', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_DOW_UIN_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1188' AND lang_code='tam';
UPDATE master.template
SET "name"='Download e-UIN Failure SMS', descr='Download e-UIN Failure SMS', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your request for downloading your e-UIN has failed. Please try again later. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_DOW_UIN_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1189' AND lang_code='eng';
UPDATE master.template
SET "name"='تنزيل رسائل فشل e-UIN SMS', descr='تنزيل رسائل فشل e-UIN SMS', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، لقد فشل طلبك لتنزيل e-UIN الخاص بك. الرجاء معاودة المحاولة في وقت لاحق. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_DOW_UIN_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1189' AND lang_code='ara';
UPDATE master.template
SET "name"='e-UIN தோல்வி SMS ஐப் பதிவிறக்கவும்', descr='e-UIN தோல்வி SMS ஐப் பதிவிறக்கவும்', file_format_code='txt', model='velocity', file_txt='வணக்கம் $ fullName_eng, உங்கள் e-UIN ஐப் பதிவிறக்குவதற்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. பிறகு முயற்சிக்கவும். நன்றி! ', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_DOW_UIN_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1189' AND lang_code='tam';
UPDATE master.template
SET "name"='e-UIN ವೈಫಲ್ಯದ SMS ಅನ್ನು ಡೌನ್‌ಲೋಡ್ ಮಾಡಿ', descr='e-UIN ವೈಫಲ್ಯದ SMS ಅನ್ನು ಡೌನ್‌ಲೋಡ್ ಮಾಡಿ', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ನಿಮ್ಮ e-UIN ಅನ್ನು ಡೌನ್‌ಲೋಡ್ ಮಾಡಲು ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. ದಯವಿಟ್ಟು ನಂತರ ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ. ಧನ್ಯವಾದಗಳು! ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_DOW_UIN_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1189' AND lang_code='kan';
UPDATE master.template
SET "name"='ई-यूआईएन विफलता के लिए एसएमएस डाउनलोड करें', descr='ई-यूआईएन विफलता के लिए एसएमएस डाउनलोड करें', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, आपका ई-यूआईएन डाउनलोड करने का आपका अनुरोध विफल हो गया। बाद में पुन: प्रयास करें। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_DOW_UIN_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1189' AND lang_code='hin';
UPDATE master.template
SET "name"='Télécharger le SMS d''échec e-UIN', descr='Télécharger le SMS d''échec e-UIN', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre demande de téléchargement de votre e-UIN a échoué. Veuillez réessayer plus tard. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_DOW_UIN_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1189' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure in Locking of Auth Types Email', descr='Failure in Locking of Auth Types Email', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your requested for Locking AUTH type(s) has failed. Please try again later. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_LOCK_AUTH_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1190' AND lang_code='eng';
UPDATE master.template
SET "name"='அங்கீகார வகை மின்னஞ்சலைப் பூட்டுவதில் தோல்வி', descr='அங்கீகார வகை மின்னஞ்சலைப் பூட்டுவதில் தோல்வி', file_format_code='txt', model='velocity', file_txt='வணக்கம் $ fullName_eng, உங்கள் e-UIN ஐப் பதிவிறக்குவதற்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. பிறகு முயற்சிக்கவும். நன்றி! ', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_LOCK_AUTH_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1190' AND lang_code='tam';
UPDATE master.template
SET "name"='प्रमाणीकरण प्रकार ईमेल लॉक करने में विफल', descr='प्रमाणीकरण प्रकार ईमेल लॉक करने में विफल', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, आपका ई-यूआईएन डाउनलोड करने का आपका अनुरोध विफल हो गया। बाद में पुन: प्रयास करें। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_LOCK_AUTH_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1190' AND lang_code='hin';
UPDATE master.template
SET "name"='فشل في قفل البريد الإلكتروني لأنواع المصادقة', descr='فشل في قفل البريد الإلكتروني لأنواع المصادقة', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، لقد فشل طلبك لقفل نوع (أنواع) AUTH. الرجاء معاودة المحاولة في وقت لاحق. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_LOCK_AUTH_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1190' AND lang_code='ara';
UPDATE master.template
SET "name"='Échec du verrouillage des e-mails des types d''authentification', descr='Échec du verrouillage des e-mails des types d''authentification', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre ou vos types de verrouillage AUTH demandés ont échoué. Veuillez réessayer plus tard. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_LOCK_AUTH_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1190' AND lang_code='fra';
UPDATE master.template
SET "name"='ದೃಢೀಕರಣ ಪ್ರಕಾರಗಳ ಇಮೇಲ್ ಅನ್ನು ಲಾಕ್ ಮಾಡುವಲ್ಲಿ ವಿಫಲವಾಗಿದೆ', descr='ದೃಢೀಕರಣ ಪ್ರಕಾರಗಳ ಇಮೇಲ್ ಅನ್ನು ಲಾಕ್ ಮಾಡುವಲ್ಲಿ ವಿಫಲವಾಗಿದೆ', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ನಿಮ್ಮ e-UIN ಅನ್ನು ಡೌನ್‌ಲೋಡ್ ಮಾಡಲು ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. ದಯವಿಟ್ಟು ನಂತರ ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ. ಧನ್ಯವಾದಗಳು! ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_LOCK_AUTH_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1190' AND lang_code='kan';
UPDATE master.template
SET "name"='प्रमाणीकरण प्रकार की ईमेल सामग्री को लॉक करने में विफल', descr='प्रमाणीकरण प्रकार की ईमेल सामग्री को लॉक करने में विफल', file_format_code='txt', model='velocity', file_txt='AUTH (ओं) के अनुसार लॉक विफल रहा', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_LOCK_AUTH_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1191' AND lang_code='hin';
UPDATE master.template
SET "name"='அங்கீகார வகைகளின் மின்னஞ்சல் உள்ளடக்கத்தைப் பூட்டுவதில் தோல்வி', descr='அங்கீகார வகைகளின் மின்னஞ்சல் உள்ளடக்கத்தைப் பூட்டுவதில் தோல்வி', file_format_code='txt', model='velocity', file_txt='AUTH (கள்) படி பூட்டு தோல்வியடைந்தது', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_LOCK_AUTH_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1191' AND lang_code='tam';
UPDATE master.template
SET "name"='ದೃಢೀಕರಣ ಪ್ರಕಾರಗಳ ಇಮೇಲ್ ವಿಷಯದ ಲಾಕ್ ಮಾಡುವಲ್ಲಿ ವಿಫಲವಾಗಿದೆ', descr='ದೃಢೀಕರಣ ಪ್ರಕಾರಗಳ ಇಮೇಲ್ ವಿಷಯದ ಲಾಕ್ ಮಾಡುವಲ್ಲಿ ವಿಫಲವಾಗಿದೆ', file_format_code='txt', model='velocity', file_txt='AUTH ಪ್ರಕಾರ(ಗಳ) ಲಾಕ್ ವಿಫಲವಾಗಿದೆ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_LOCK_AUTH_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1191' AND lang_code='kan';
UPDATE master.template
SET "name"='فشل في قفل موضوع البريد الإلكتروني لأنواع المصادقة', descr='فشل في قفل موضوع البريد الإلكتروني لأنواع المصادقة', file_format_code='txt', model='velocity', file_txt='فشل تأمين نوع (أنواع) AUTH', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_LOCK_AUTH_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1191' AND lang_code='ara';
UPDATE master.template
SET "name"='Échec du verrouillage des types d''authentification Objet de l''e-mail', descr='Échec du verrouillage des types d''authentification Objet de l''e-mail', file_format_code='txt', model='velocity', file_txt='Échec du verrouillage du ou des types AUTH', module_id='10006', module_name='Resident Services', template_typ_code='RS_LOCK_AUTH_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1191' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure in Locking of Auth Types Email Subject', descr='Failure in Locking of Auth Types Email Subject', file_format_code='txt', model='velocity', file_txt='Locking of AUTH Type(s) Failed', module_id='10006', module_name='Resident Services', template_typ_code='RS_LOCK_AUTH_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1191' AND lang_code='eng';
UPDATE master.template
SET "name"='فشل في قفل أنواع المصادقة SMS', descr='فشل في قفل أنواع المصادقة SMS', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، لقد فشل طلبك لقفل نوع (أنواع) AUTH. الرجاء معاودة المحاولة في وقت لاحق. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_LOCK_AUTH_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1192' AND lang_code='ara';
UPDATE master.template
SET "name"='Échec du verrouillage des types d''authentification SMS', descr='Échec du verrouillage des types d''authentification SMS', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre ou vos types de verrouillage AUTH demandés ont échoué. Veuillez réessayer plus tard. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_LOCK_AUTH_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1192' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure in Locking of Auth Types SMS', descr='Failure in Locking of Auth Types SMS', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your requested for Locking AUTH type(s) has failed. Please try again later. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_LOCK_AUTH_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1192' AND lang_code='eng';
UPDATE master.template
SET "name"='प्रमाणीकरण प्रकार एसएमएस लॉक करने में विफल', descr='प्रमाणीकरण प्रकार एसएमएस लॉक करने में विफल', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, आप AUTH प्रकार (प्रकारों) को लॉक करने का अनुरोध करने में विफल रहे। बाद में पुन: प्रयास करें। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_LOCK_AUTH_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1192' AND lang_code='hin';
UPDATE master.template
SET "name"='ದೃಢೀಕರಣ ಪ್ರಕಾರಗಳ SMS ಲಾಕ್ ಮಾಡುವಲ್ಲಿ ವಿಫಲವಾಗಿದೆ', descr='ದೃಢೀಕರಣ ಪ್ರಕಾರಗಳ SMS ಲಾಕ್ ಮಾಡುವಲ್ಲಿ ವಿಫಲವಾಗಿದೆ', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, AUTH ಪ್ರಕಾರ(ಗಳನ್ನು) ಲಾಕ್ ಮಾಡಲು ನೀವು ವಿನಂತಿಸಿದ್ದು ವಿಫಲವಾಗಿದೆ. ದಯವಿಟ್ಟು ನಂತರ ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_LOCK_AUTH_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1192' AND lang_code='kan';
UPDATE master.template
SET "name"='எஸ்எம்எஸ் அங்கீகார வகைகளைப் பூட்டுவதில் தோல்வி', descr='எஸ்எம்எஸ் அங்கீகார வகைகளைப் பூட்டுவதில் தோல்வி', file_format_code='txt', model='velocity', file_txt='ஹாய் $ fullName_eng, நீங்கள் AUTH வகையை (களை) பூட்டுமாறு கோரத் தவறிவிட்டீர்கள். பிறகு முயற்சிக்கவும். நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_LOCK_AUTH_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1192' AND lang_code='tam';
UPDATE master.template
SET "name"='மறுஅச்சு கோரிக்கை தோல்வி மின்னஞ்சல்', descr='மறுஅச்சு கோரிக்கை தோல்வி மின்னஞ்சல்', file_format_code='txt', model='velocity', file_txt='வணக்கம் $ fullName_eng, "UIN கார்டு மறுபதிப்பு"க்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. பிறகு முயற்சிக்கவும். நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_UIN_RPR_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1193' AND lang_code='tam';
UPDATE master.template
SET "name"='إعادة طباعة البريد الإلكتروني لطلب الفشل', descr='إعادة طباعة البريد الإلكتروني لطلب الفشل', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، لقد فشل طلبك بشأن "إعادة طباعة بطاقة UIN". الرجاء معاودة المحاولة في وقت لاحق. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_UIN_RPR_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1193' AND lang_code='ara';
UPDATE master.template
SET "name"='E-mail d''échec de la demande de réimpression', descr='E-mail d''échec de la demande de réimpression', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre demande de «réimpression de la carte UIN» a échoué. Veuillez réessayer plus tard. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_RPR_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1193' AND lang_code='fra';
UPDATE master.template
SET "name"='Reprint Request Failure Email', descr='Reprint Request Failure Email', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your request for "Reprint of UIN Card" has failed. Please try again later. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_RPR_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1193' AND lang_code='eng';
UPDATE master.template
SET "name"='ಮರುಮುದ್ರಣ ವಿನಂತಿ ವಿಫಲ ಇಮೇಲ್', descr='ಮರುಮುದ್ರಣ ವಿನಂತಿ ವಿಫಲ ಇಮೇಲ್', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, "UIN ಕಾರ್ಡ್‌ನ ಮರುಮುದ್ರಣ" ಗಾಗಿ ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. ದಯವಿಟ್ಟು ನಂತರ ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_UIN_RPR_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1193' AND lang_code='kan';
UPDATE master.template
SET "name"='पुनर्मुद्रण अनुरोध विफल ईमेल', descr=' पुनर्मुद्रण अनुरोध विफल ईमेल', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, "यूआईएन कार्ड पुनर्मुद्रण" के लिए आपका अनुरोध विफल हो गया। बाद में पुन: प्रयास करें। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_UIN_RPR_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1193' AND lang_code='hin';
UPDATE master.template
SET "name"='ಮರುಮುದ್ರಣ ವಿನಂತಿಯ ವಿಫಲತೆಯ ಇಮೇಲ್ ವಿಷಯ', descr='ಮರುಮುದ್ರಣ ವಿನಂತಿಯ ವಿಫಲತೆಯ ಇಮೇಲ್ ವಿಷಯ', file_format_code='txt', model='velocity', file_txt='ಮರುಮುದ್ರಣ ವಿನಂತಿ ವಿಫಲವಾಗಿದೆ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_UIN_RPR_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1194' AND lang_code='kan';
UPDATE master.template
SET "name"='Reprint Request Failure Email Subject', descr='Reprint Request Failure Email Subject', file_format_code='txt', model='velocity', file_txt='Reprint Request Failure', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_RPR_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1194' AND lang_code='eng';
UPDATE master.template
SET "name"='மறுபதிப்பு கோரிக்கை தோல்வியடைந்தால் மின்னஞ்சல்', descr='மறுபதிப்பு கோரிக்கை தோல்வியடைந்தால் மின்னஞ்சல்', file_format_code='txt', model='velocity', file_txt='மறுபதிப்பு கோரிக்கை தோல்வியடைந்தது', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_UIN_RPR_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1194' AND lang_code='tam';
UPDATE master.template
SET "name"='ईमेल पुनर्मुद्रण अनुरोध की विफलता के अधीन', descr='ईमेल पुनर्मुद्रण अनुरोध की विफलता के अधीन', file_format_code='txt', model='velocity', file_txt='पुनर्मुद्रण अनुरोध विफल रहा', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_UIN_RPR_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1194' AND lang_code='hin';
UPDATE master.template
SET "name"='Échec de la demande de réimpression Objet de l''e-mail', descr='Échec de la demande de réimpression Objet de l''e-mail', file_format_code='txt', model='velocity', file_txt='Échec de la demande de réimpression', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_RPR_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1194' AND lang_code='fra';
UPDATE master.template
SET "name"='إعادة طباعة طلب فشل عنوان البريد الإلكتروني', descr='إعادة طباعة طلب فشل عنوان البريد الإلكتروني', file_format_code='txt', model='velocity', file_txt='فشل طلب إعادة الطباعة', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_UIN_RPR_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1194' AND lang_code='ara';
UPDATE master.template
SET "name"='Reprint Request Failure SMS', descr='Reprint Request Failure SMS', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your request for "Reprint of UIN Card" has failed. Please try again later. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_RPR_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1195' AND lang_code='eng';
UPDATE master.template
SET "name"='पुनर्मुद्रण अनुरोध विफल एसएमएस', descr='पुनर्मुद्रण अनुरोध विफल एसएमएस', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, "यूआईएन कार्ड पुनर्मुद्रण" के लिए आपका अनुरोध विफल हो गया। बाद में पुन: प्रयास करें। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_UIN_RPR_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1195' AND lang_code='hin';
UPDATE master.template
SET "name"='மறுபதிப்பு கோரிக்கை தோல்வியுற்ற SMS', descr='மறுபதிப்பு கோரிக்கை தோல்வியுற்ற SMS', file_format_code='txt', model='velocity', file_txt='வணக்கம் $ fullName_eng, "UIN கார்டு மறுபதிப்பு"க்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. பிறகு முயற்சிக்கவும். நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_UIN_RPR_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1195' AND lang_code='tam';
UPDATE master.template
SET "name"='ಮರುಮುದ್ರಣ ವಿನಂತಿ ವಿಫಲ SMS', descr='ಮರುಮುದ್ರಣ ವಿನಂತಿ ವಿಫಲ SMS', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, "UIN ಕಾರ್ಡ್‌ನ ಮರುಮುದ್ರಣ" ಗಾಗಿ ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. ದಯವಿಟ್ಟು ನಂತರ ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_UIN_RPR_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1195' AND lang_code='kan';
UPDATE master.template
SET "name"='إعادة طباعة فشل طلب الرسائل القصيرة', descr='إعادة طباعة فشل طلب الرسائل القصيرة', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، لقد فشل طلبك بشأن "إعادة طباعة بطاقة UIN". الرجاء معاودة المحاولة في وقت لاحق. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_UIN_RPR_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1195' AND lang_code='ara';
UPDATE master.template
SET "name"='SMS d''échec de la demande de réimpression', descr='SMS d''échec de la demande de réimpression', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre demande de «réimpression de la carte UIN» a échoué. Veuillez réessayer plus tard. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_RPR_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1195' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure in Unlocking of Auth Types Email', descr='Failure in Unlocking of Auth Types Email', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your requested for unlocking AUTH type(s) has failed. Please try again later. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UNLOCK_AUTH_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1196' AND lang_code='eng';
UPDATE master.template
SET "name"='فشل في إلغاء قفل البريد الإلكتروني لأنواع المصادقة', descr='فشل في إلغاء قفل البريد الإلكتروني لأنواع المصادقة', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، لقد فشل طلبك لإلغاء تأمين نوع (أنواع) AUTH. الرجاء معاودة المحاولة في وقت لاحق. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_UNLOCK_AUTH_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1196' AND lang_code='ara';
UPDATE master.template
SET "name"='உறுதிப்படுத்தல் வகை மின்னஞ்சலைத் திறக்க முடியவில்லை', descr='உறுதிப்படுத்தல் வகை மின்னஞ்சலைத் திறக்க முடியவில்லை', file_format_code='txt', model='velocity', file_txt='ஹாய் $ fullName_eng, நீங்கள் திறக்கக் கோரிய AUTH வகை (கள்) தோல்வியடைந்தது. பிறகு முயற்சிக்கவும். நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_UNLOCK_AUTH_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1196' AND lang_code='tam';
UPDATE master.template
SET "name"='Échec du déverrouillage de la messagerie électronique des types d''authentification', descr='Échec du déverrouillage de la messagerie électronique des types d''authentification', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre demande de déverrouillage de type (s) AUTH a échoué. Veuillez réessayer plus tard. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UNLOCK_AUTH_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1196' AND lang_code='fra';
UPDATE master.template
SET "name"='ದೃಢೀಕರಣ ಪ್ರಕಾರದ ಇಮೇಲ್ ಅನ್ನು ಅನ್‌ಲಾಕ್ ಮಾಡುವಲ್ಲಿ ವಿಫಲವಾಗಿದೆ', descr='ದೃಢೀಕರಣ ಪ್ರಕಾರದ ಇಮೇಲ್ ಅನ್ನು ಅನ್‌ಲಾಕ್ ಮಾಡುವಲ್ಲಿ ವಿಫಲವಾಗಿದೆ', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, AUTH ಪ್ರಕಾರ(ಗಳು) ಅನ್‌ಲಾಕ್ ಮಾಡಲು ನೀವು ವಿನಂತಿಸಿದ್ದು ವಿಫಲವಾಗಿದೆ. ದಯವಿಟ್ಟು ನಂತರ ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_UNLOCK_AUTH_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1196' AND lang_code='kan';
UPDATE master.template
SET "name"='पुष्टिकरण प्रकार ईमेल अनलॉक करने में विफल', descr='पुष्टिकरण प्रकार ईमेल अनलॉक करने में विफल', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, AUTH प्रकार (प्रकारों) जिसे आपने अनलॉक करने का अनुरोध किया था, विफल रहा। बाद में पुन: प्रयास करें। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_UNLOCK_AUTH_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1196' AND lang_code='hin';
UPDATE master.template
SET "name"='ದೃಢೀಕರಣ ಪ್ರಕಾರಗಳ ಇಮೇಲ್ ವಿಷಯದ ಅನ್‌ಲಾಕ್‌ನಲ್ಲಿ ವಿಫಲವಾಗಿದೆ', descr='ದೃಢೀಕರಣ ಪ್ರಕಾರಗಳ ಇಮೇಲ್ ವಿಷಯದ ಅನ್‌ಲಾಕ್‌ನಲ್ಲಿ ವಿಫಲವಾಗಿದೆ', file_format_code='txt', model='velocity', file_txt='AUTH ಪ್ರಕಾರ(ಗಳ) ಅನ್‌ಲಾಕ್ ವಿಫಲವಾಗಿದೆ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_UNLOCK_AUTH_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1197' AND lang_code='kan';
UPDATE master.template
SET "name"='प्रमाणीकरण प्रकार ईमेल सामग्री को अनलॉक करने में विफल', descr='प्रमाणीकरण प्रकार ईमेल सामग्री को अनलॉक करने में विफल', file_format_code='txt', model='velocity', file_txt='AUTH (ओं) के अनुसार अनलॉक विफल रहा', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_UNLOCK_AUTH_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1197' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure in Unlocking of Auth Types Email Subject', descr='Failure in Unlocking of Auth Types Email Subject', file_format_code='txt', model='velocity', file_txt='Unlocking of AUTH Type(s) Failed', module_id='10006', module_name='Resident Services', template_typ_code='RS_UNLOCK_AUTH_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1197' AND lang_code='eng';
UPDATE master.template
SET "name"='Échec du déverrouillage du sujet de l''e-mail des types d''authentification', descr='Échec du déverrouillage du sujet de l''e-mail des types d''authentification', file_format_code='txt', model='velocity', file_txt='Le déverrouillage du ou des types AUTH a échoué', module_id='10006', module_name='Resident Services', template_typ_code='RS_UNLOCK_AUTH_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1197' AND lang_code='fra';
UPDATE master.template
SET "name"='فشل في إلغاء تأمين موضوع البريد الإلكتروني لأنواع المصادقة', descr='فشل في إلغاء تأمين موضوع البريد الإلكتروني لأنواع المصادقة', file_format_code='txt', model='velocity', file_txt='فشل فتح نوع (أنواع) AUTH', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_UNLOCK_AUTH_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1197' AND lang_code='ara';
UPDATE master.template
SET "name"='அங்கீகார வகைகள் மின்னஞ்சல் உள்ளடக்கத்தைத் திறக்க முடியவில்லை', descr='அங்கீகார வகைகள் மின்னஞ்சல் உள்ளடக்கத்தைத் திறக்க முடியவில்லை', file_format_code='txt', model='velocity', file_txt='AUTH (கள்) படி திறக்க முடியவில்லை', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_UNLOCK_AUTH_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1197' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure in Unlocking of Auth Types SMS', descr='Failure in Unlocking of Auth Types SMS', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your requested for unlocking AUTH type(s) has failed. Please try again later. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UNLOCK_AUTH_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1198' AND lang_code='eng';
UPDATE master.template
SET "name"='فشل في فتح قفل أنواع المصادقة SMS', descr='فشل في فتح قفل أنواع المصادقة SMS', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، لقد فشل طلبك لإلغاء تأمين نوع (أنواع) AUTH. الرجاء معاودة المحاولة في وقت لاحق. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_UNLOCK_AUTH_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1198' AND lang_code='ara';
UPDATE master.template
SET "name"='அங்கீகார வகைக்கான எஸ்எம்எஸ் திறக்க முடியவில்லை', descr='அங்கீகார வகைக்கான எஸ்எம்எஸ் திறக்க முடியவில்லை', file_format_code='txt', model='velocity', file_txt='ஹாய் $ fullName_eng, நீங்கள் திறக்கக் கோரிய AUTH வகை (கள்) தோல்வியடைந்தது. பிறகு முயற்சிக்கவும். நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_UNLOCK_AUTH_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1198' AND lang_code='tam';
UPDATE master.template
SET "name"='ದೃಢೀಕರಣ ಪ್ರಕಾರದ SMS ಅನ್‌ಲಾಕ್ ಮಾಡುವಲ್ಲಿ ವಿಫಲವಾಗಿದೆ', descr='ದೃಢೀಕರಣ ಪ್ರಕಾರದ SMS ಅನ್‌ಲಾಕ್ ಮಾಡುವಲ್ಲಿ ವಿಫಲವಾಗಿದೆ', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, AUTH ಪ್ರಕಾರ(ಗಳು) ಅನ್‌ಲಾಕ್ ಮಾಡಲು ನೀವು ವಿನಂತಿಸಿದ್ದು ವಿಫಲವಾಗಿದೆ. ದಯವಿಟ್ಟು ನಂತರ ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_UNLOCK_AUTH_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1198' AND lang_code='kan';
UPDATE master.template
SET "name"='प्रमाणीकरण प्रकार के लिए एसएमएस अनलॉक करने में विफल', descr='प्रमाणीकरण प्रकार के लिए एसएमएस अनलॉक करने में विफल', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, AUTH प्रकार (प्रकारों) जिसे आपने अनलॉक करने का अनुरोध किया था, विफल रहा। बाद में पुन: प्रयास करें। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_UNLOCK_AUTH_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1198' AND lang_code='hin';
UPDATE master.template
SET "name"='Échec de déverrouillage des types d''authentification SMS', descr='Échec de déverrouillage des types d''authentification SMS', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre demande de déverrouillage de type (s) AUTH a échoué. Veuillez réessayer plus tard. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UNLOCK_AUTH_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1198' AND lang_code='fra';
UPDATE master.template
SET "name"='VID Generation Failure Email', descr='VID Generation Failure Email', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, VID generation for your UIN failed. Please try again later. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_GEN_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1199' AND lang_code='eng';
UPDATE master.template
SET "name"='E-mail d''échec de génération de VID', descr='E-mail d''échec de génération de VID', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, La génération de VID pour votre UIN a échoué. Veuillez réessayer plus tard. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_GEN_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1199' AND lang_code='fra';
UPDATE master.template
SET "name"='فشل إنشاء VID', descr='فشل إنشاء VID', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، فشل إنشاء VID لـ UIN الخاص بك. الرجاء معاودة المحاولة في وقت لاحق. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_VIN_GEN_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1199' AND lang_code='ara';
UPDATE master.template
SET "name"='VID ಜನರೇಷನ್ ವೈಫಲ್ಯ ಇಮೇಲ್', descr='VID ಜನರೇಷನ್ ವೈಫಲ್ಯ ಇಮೇಲ್', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ನಿಮ್ಮ UIN ಗಾಗಿ VID ಉತ್ಪಾದನೆ ವಿಫಲವಾಗಿದೆ. ದಯವಿಟ್ಟು ನಂತರ ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_VIN_GEN_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1199' AND lang_code='kan';
UPDATE master.template
SET "name"='VID जनरेशन विफलता ईमेल', descr='VID जनरेशन विफलता ईमेल', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, आपके UIN के लिए VID जनरेशन विफल रहा। बाद में पुन: प्रयास करें। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_VIN_GEN_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1199' AND lang_code='hin';
UPDATE master.template
SET "name"='VID தலைமுறை தோல்வி மின்னஞ்சல்', descr='VID தலைமுறை தோல்வி மின்னஞ்சல்', file_format_code='txt', model='velocity', file_txt='ஹாய் $ fullName_eng, உங்கள் UINக்கு VID உருவாக்கம் தோல்வியடைந்தது. பிறகு முயற்சிக்கவும். நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_VIN_GEN_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1199' AND lang_code='tam';
UPDATE master.template
SET "name"='VID ಜನರೇಷನ್ ವೈಫಲ್ಯ ಇಮೇಲ್ ವಿಷಯ', descr='VID ಜನರೇಷನ್ ವೈಫಲ್ಯ ಇಮೇಲ್ ವಿಷಯ', file_format_code='txt', model='velocity', file_txt='VID ಜನರೇಷನ್ ವೈಫಲ್ಯ ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_VIN_GEN_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1200' AND lang_code='kan';
UPDATE master.template
SET "name"='VID जनरेशन विफलता ईमेल सामग्री', descr='VID जनरेशन विफलता ईमेल सामग्री', file_format_code='txt', model='velocity', file_txt='वीआईडी जनरेशन विफलता', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_VIN_GEN_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1200' AND lang_code='hin';
UPDATE master.template
SET "name"='VID Generation Failure Email Subject', descr='VID Generation Failure Email Subject', file_format_code='txt', model='velocity', file_txt='فشل إنشاء VID', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_VIN_GEN_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1200' AND lang_code='ara';
UPDATE master.template
SET "name"='Objet de l''e-mail d''échec de génération de VID', descr='Objet de l''e-mail d''échec de génération de VID', file_format_code='txt', model='velocity', file_txt='Échec de génération de VID', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_GEN_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1200' AND lang_code='fra';
UPDATE master.template
SET "name"='VID Generation Failure Email Subject', descr='VID Generation Failure Email Subject', file_format_code='txt', model='velocity', file_txt='VID Generation Failure', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_GEN_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1200' AND lang_code='eng';
UPDATE master.template
SET "name"='VID தலைமுறை தோல்வி மின்னஞ்சல் உள்ளடக்கம்', descr='VID தலைமுறை தோல்வி மின்னஞ்சல் உள்ளடக்கம்', file_format_code='txt', model='velocity', file_txt='விஐடி தலைமுறை தோல்வி', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_VIN_GEN_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1200' AND lang_code='tam';
UPDATE master.template
SET "name"='वीआईडी जनरेशन विफलता एसएमएस', descr='वीआईडी जनरेशन विफलता एसएमएस', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, आपके UIN के लिए VID जनरेशन विफल रहा। बाद में पुन: प्रयास करें। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_VIN_GEN_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1201' AND lang_code='hin';
UPDATE master.template
SET "name"='VID ಜನರೇಷನ್ ವೈಫಲ್ಯ SMS', descr='VID ಜನರೇಷನ್ ವೈಫಲ್ಯ SMS', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ನಿಮ್ಮ UIN ಗಾಗಿ VID ಉತ್ಪಾದನೆ ವಿಫಲವಾಗಿದೆ. ದಯವಿಟ್ಟು ನಂತರ ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_VIN_GEN_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1201' AND lang_code='kan';
UPDATE master.template
SET "name"='விஐடி தலைமுறை தோல்வி எஸ்எம்எஸ்', descr='விஐடி தலைமுறை தோல்வி எஸ்எம்எஸ்', file_format_code='txt', model='velocity', file_txt='ஹாய் $ fullName_eng, உங்கள் UINக்கு VID உருவாக்கம் தோல்வியடைந்தது. பிறகு முயற்சிக்கவும். நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_VIN_GEN_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1201' AND lang_code='tam';
UPDATE master.template
SET "name"='VID Generation Failure SMS', descr='VID Generation Failure SMS', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, VID generation for your UIN failed. Please try again later. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_GEN_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1201' AND lang_code='eng';
UPDATE master.template
SET "name"='SMS d''échec de génération de VID', descr='SMS d''échec de génération de VID', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, La génération de VID pour votre UIN a échoué. Veuillez réessayer plus tard. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_GEN_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1201' AND lang_code='fra';
UPDATE master.template
SET "name"='فشل إنشاء VID SMS', descr='فشل إنشاء VID SMS', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، فشل إنشاء VID لـ UIN الخاص بك. الرجاء معاودة المحاولة في وقت لاحق. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_VIN_GEN_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1201' AND lang_code='ara';
UPDATE master.template
SET "name"='البريد الإلكتروني لفشل إبطال VID', descr='البريد الإلكتروني لفشل إبطال VID', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، لقد فشل إبطال $ VID الخاص بك. الرجاء معاودة المحاولة في وقت لاحق. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_VIN_REV_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1202' AND lang_code='ara';
UPDATE master.template
SET "name"='விஐடி மீட்டெடுப்பு மின்னஞ்சல் தோல்வியடைந்தது', descr='விஐடி மீட்டெடுப்பு மின்னஞ்சல் தோல்வியடைந்தது', file_format_code='txt', model='velocity', file_txt='வணக்கம் $ fullName_eng, உங்கள் $ VID மீட்டெடுப்பு தோல்வியடைந்தது. பிறகு முயற்சிக்கவும். நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_VIN_REV_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1202' AND lang_code='tam';
UPDATE master.template
SET "name"='E-mail d''échec de révocation de VID', descr='E-mail d''échec de révocation de VID', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre révocation de $VID a échoué. Veuillez réessayer plus tard. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_REV_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1202' AND lang_code='fra';
UPDATE master.template
SET "name"='VID Revocation Failure Email', descr='VID Revocation Failure Email', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your $VID revokation has failed. Please try again later. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_REV_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1202' AND lang_code='eng';
UPDATE master.template
SET "name"='ವಿಐಡಿ ಹಿಂಪಡೆಯುವಿಕೆ ವಿಫಲ ಇಮೇಲ್', descr='ವಿಐಡಿ ಹಿಂಪಡೆಯುವಿಕೆ ವಿಫಲ ಇಮೇಲ್', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ನಿಮ್ಮ $VID ಹಿಂಪಡೆಯುವಿಕೆ ವಿಫಲವಾಗಿದೆ. ದಯವಿಟ್ಟು ನಂತರ ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_VIN_REV_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1202' AND lang_code='kan';
UPDATE master.template
SET "name"='VID पुनर्प्राप्ति विफल ईमेल ', descr='VID पुनर्प्राप्ति विफल ईमेल', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, आपकी $ VID पुनर्प्राप्ति विफल रही। बाद में पुन: प्रयास करें। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_VIN_REV_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1202' AND lang_code='hin';
UPDATE master.template
SET "name"='VID पुनर्प्राप्ति विफलता ईमेल सामग्री ', descr='VID पुनर्प्राप्ति विफलता ईमेल सामग्री', file_format_code='txt', model='velocity', file_txt='वीआईडी पुनर्प्राप्ति विफल', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_VIN_REV_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1203' AND lang_code='hin';
UPDATE master.template
SET "name"='ವಿಐಡಿ ಹಿಂಪಡೆಯುವಿಕೆ ವೈಫಲ್ಯ ಇಮೇಲ್ ವಿಷಯ ', descr='ವಿಐಡಿ ಹಿಂಪಡೆಯುವಿಕೆ ವೈಫಲ್ಯ ಇಮೇಲ್ ವಿಷಯ', file_format_code='txt', model='velocity', file_txt='ವಿಐಡಿ ಹಿಂಪಡೆಯುವಿಕೆ ವಿಫಲವಾಗಿದೆ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_VIN_REV_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1203' AND lang_code='kan';
UPDATE master.template
SET "name"='விஐடி மீட்டெடுப்பு தோல்வி மின்னஞ்சல் உள்ளடக்கம்', descr='விஐடி மீட்டெடுப்பு தோல்வி மின்னஞ்சல் உள்ளடக்கம்', file_format_code='txt', model='velocity', file_txt='VID மீட்டெடுப்பு தோல்வியடைந்தது', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_VIN_REV_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1203' AND lang_code='tam';
UPDATE master.template
SET "name"='موضوع البريد الإلكتروني لفشل إبطال VID', descr='موضوع البريد الإلكتروني لفشل إبطال VID', file_format_code='txt', model='velocity', file_txt='فشل إبطال VID', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_VIN_REV_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1203' AND lang_code='ara';
UPDATE master.template
SET "name"='Objet de l''e-mail d''échec de la révocation du VID', descr='Objet de l''e-mail d''échec de la révocation du VID', file_format_code='txt', model='velocity', file_txt='Échec de révocation de VID', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_REV_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1203' AND lang_code='fra';
UPDATE master.template
SET "name"='VID Revocation Failure Email Subject', descr='VID Revocation Failure Email Subject', file_format_code='txt', model='velocity', file_txt='VID Revokation Failure', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_REV_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1203' AND lang_code='eng';
UPDATE master.template
SET "name"='SMS d''échec de révocation de VID', descr='SMS d''échec de révocation de VID', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre révocation de $VID a échoué. Veuillez réessayer plus tard. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_REV_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1204' AND lang_code='fra';
UPDATE master.template
SET "name"='ವಿಐಡಿ ಹಿಂಪಡೆಯುವಿಕೆ ವೈಫಲ್ಯ SMS ', descr='ವಿಐಡಿ ಹಿಂಪಡೆಯುವಿಕೆ ವೈಫಲ್ಯ SMS', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ನಿಮ್ಮ $VID ಹಿಂಪಡೆಯುವಿಕೆ ವಿಫಲವಾಗಿದೆ. ದಯವಿಟ್ಟು ನಂತರ ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_VIN_REV_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1204' AND lang_code='kan';
UPDATE master.template
SET "name"='वीआईडी पुनर्प्राप्ति विफलता एसएमएस ', descr='वीआईडी ​​​​पुनर्प्राप्ति विफलता एसएमएस', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, आपकी $ VID पुनर्प्राप्ति विफल रही। बाद में पुन: प्रयास करें। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_VIN_REV_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1204' AND lang_code='hin';
UPDATE master.template
SET "name"='VID Revocation Failure SMS', descr='VID Revocation Failure SMS', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your $VID revokation has failed. Please try again later. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_VIN_REV_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1204' AND lang_code='eng';
UPDATE master.template
SET "name"='விஐடி மீட்டெடுப்பு தோல்வி எஸ்எம்எஸ் ', descr='விஐடி மீட்டெடுப்பு தோல்வி எஸ்எம்எஸ்', file_format_code='txt', model='velocity', file_txt='வணக்கம் $ fullName_eng, உங்கள் $ VID மீட்டெடுப்பு தோல்வியடைந்தது. பிறகு முயற்சிக்கவும். நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_VIN_REV_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1204' AND lang_code='tam';
UPDATE master.template
SET "name"='فشل إبطال VID SMS', descr='فشل إبطال VID SMS', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، لقد فشل إبطال $ VID الخاص بك. الرجاء معاودة المحاولة في وقت لاحق. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_VIN_REV_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1204' AND lang_code='ara';
UPDATE master.template
SET "name"='UIN Update Request Placed Successfully Email', descr='UIN Update Request Placed Successfully Email', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your request for "UIN Update" has been successfully placed. Your RID (Req Number) is $RID for tracking. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_UPDATE_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1205' AND lang_code='eng';
UPDATE master.template
SET "name"='UIN अपडेट अनुरोध सफलतापूर्वक ईमेल किया गया था', descr='UIN अपडेट अनुरोध सफलतापूर्वक ईमेल किया गया था', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, "यूआईएन अपडेट" के लिए आपका अनुरोध सफलतापूर्वक कर दिया गया है। ट्रैकिंग के लिए आपका RID (Req number) $ RID है। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_UIN_UPDATE_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1205' AND lang_code='hin';
UPDATE master.template
SET "name"='UIN புதுப்பிப்பு கோரிக்கை வெற்றிகரமாக மின்னஞ்சல் அனுப்பப்பட்டது', descr='UIN புதுப்பிப்பு கோரிக்கை வெற்றிகரமாக மின்னஞ்சல் அனுப்பப்பட்டது', file_format_code='txt', model='velocity', file_txt='வணக்கம் $ fullName_eng, "UIN புதுப்பிப்பு"க்கான உங்கள் கோரிக்கை வெற்றிகரமாக வைக்கப்பட்டுள்ளது. உங்கள் RID (Req எண்) என்பது கண்காணிப்பதற்கான $ RID ஆகும். நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_UIN_UPDATE_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1205' AND lang_code='tam';
UPDATE master.template
SET "name"='UIN ನವೀಕರಣ ವಿನಂತಿಯನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಇಮೇಲ್ ಮಾಡಲಾಗಿದೆ', descr='UIN ನವೀಕರಣ ವಿನಂತಿಯನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಇಮೇಲ್ ಮಾಡಲಾಗಿದೆ', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, "UIN ಅಪ್‌ಡೇಟ್" ಗಾಗಿ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಇರಿಸಲಾಗಿದೆ. ನಿಮ್ಮ RID (Req ಸಂಖ್ಯೆ) ಟ್ರ್ಯಾಕಿಂಗ್‌ಗಾಗಿ $RID ಆಗಿದೆ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_UIN_UPDATE_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1205' AND lang_code='kan';
UPDATE master.template
SET "name"='E-mail de demande de mise à jour UIN placé avec succès', descr='E-mail de demande de mise à jour UIN placé avec succès', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre demande de «mise à jour UIN» a été envoyée avec succès. Votre RID (numéro de demande) est $RID pour le suivi. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_UPDATE_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1205' AND lang_code='fra';
UPDATE master.template
SET "name"='تم إرسال طلب تحديث UIN بنجاح عبر البريد الإلكتروني', descr='تم إرسال طلب تحديث UIN بنجاح عبر البريد الإلكتروني', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، تم تقديم طلبك لـ "تحديث UIN" بنجاح. RID الخاص بك (رقم الطلب) هو $ RID للتتبع. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_UIN_UPDATE_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1205' AND lang_code='ara';
UPDATE master.template
SET "name"='تم تقديم طلب تحديث UIN بنجاح عبر البريد الإلكتروني الموضوع', descr='تم تقديم طلب تحديث UIN بنجاح عبر البريد الإلكتروني الموضوع', file_format_code='txt', model='velocity', file_txt='تم تقديم طلب تحديث UIN بنجاح', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_UIN_UPDATE_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1206' AND lang_code='ara';
UPDATE master.template
SET "name"='UIN புதுப்பிப்பு கோரிக்கை வெற்றிகரமாக மின்னஞ்சல் உள்ளடக்கத்தை', descr='UIN புதுப்பிப்பு கோரிக்கை வெற்றிகரமாக மின்னஞ்சல் உள்ளடக்கத்தை', file_format_code='txt', model='velocity', file_txt='UIN புதுப்பிப்பு கோரிக்கை வெற்றிகரமாக வைக்கப்பட்டுள்ளது', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_UIN_UPDATE_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1206' AND lang_code='tam';
UPDATE master.template
SET "name"='UIN अपडेट अनुरोध ने ईमेल सामग्री को सफलतापूर्वक रखा', descr='UIN अपडेट अनुरोध ने ईमेल सामग्री को सफलतापूर्वक रखा', file_format_code='txt', model='velocity', file_txt='यूआईएन अपडेट अनुरोध सफलतापूर्वक रखा गया है', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_UIN_UPDATE_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1206' AND lang_code='hin';
UPDATE master.template
SET "name"='UIN ನವೀಕರಣ ವಿನಂತಿಯನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಇಮೇಲ್ ವಿಷಯವನ್ನು ಇರಿಸಲಾಗಿದೆ', descr='UIN ನವೀಕರಣ ವಿನಂತಿಯನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಇಮೇಲ್ ವಿಷಯವನ್ನು ಇರಿಸಲಾಗಿದೆ', file_format_code='txt', model='velocity', file_txt='UIN ನವೀಕರಣ ವಿನಂತಿಯನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಇರಿಸಲಾಗಿದೆ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_UIN_UPDATE_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1206' AND lang_code='kan';
UPDATE master.template
SET "name"='UIN Update Request Placed Successfully Email Subject', descr='UIN Update Request Placed Successfully Email Subject', file_format_code='txt', model='velocity', file_txt='UIN Update Request Placed Successfully', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_UPDATE_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1206' AND lang_code='eng';
UPDATE master.template
SET "name"='Sujet de la demande de mise à jour UIN placé avec succès', descr='Sujet de la demande de mise à jour UIN placé avec succès', file_format_code='txt', model='velocity', file_txt='Demande de mise à jour UIN placée avec succès', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_UPDATE_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1206' AND lang_code='fra';
UPDATE master.template
SET "name"='UIN புதுப்பிப்பு கோரிக்கை வெற்றிகரமாக SMS அனுப்பப்பட்டது', descr='UIN புதுப்பிப்பு கோரிக்கை வெற்றிகரமாக SMS அனுப்பப்பட்டது', file_format_code='txt', model='velocity', file_txt='வணக்கம் $ fullName_eng, "UIN புதுப்பிப்பு"க்கான உங்கள் கோரிக்கை வெற்றிகரமாக வைக்கப்பட்டுள்ளது. உங்கள் RID (Req எண்) என்பது கண்காணிப்பதற்கான $ RID ஆகும். நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_UIN_UPDATE_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1207' AND lang_code='tam';
UPDATE master.template
SET "name"='UIN Update Request Placed Successfully SMS', descr='UIN Update Request Placed Successfully SMS', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, Your request for "UIN Update" has been successfully placed. Your RID (Req Number) is $RID for tracking. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_UPDATE_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1207' AND lang_code='eng';
UPDATE master.template
SET "name"='यूआईएन अपडेट अनुरोध सफलतापूर्वक एसएमएस किया गया', descr='यूआईएन अपडेट अनुरोध सफलतापूर्वक एसएमएस किया गया', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, "यूआईएन अपडेट" के लिए आपका अनुरोध सफलतापूर्वक कर दिया गया है। ट्रैकिंग के लिए आपका RID (Req number) $ RID है। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_UIN_UPDATE_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1207' AND lang_code='hin';
UPDATE master.template
SET "name"='Demande de mise à jour UIN placée avec succès SMS', descr='Demande de mise à jour UIN placée avec succès SMS', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Votre demande de «mise à jour UIN» a été envoyée avec succès. Votre RID (numéro de demande) est $RID pour le suivi. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_UPDATE_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1207' AND lang_code='fra';
UPDATE master.template
SET "name"='UIN ನವೀಕರಣ ವಿನಂತಿಯನ್ನು ಯಶಸ್ವಿಯಾಗಿ SMS ಇರಿಸಲಾಗಿದೆ', descr='UIN ನವೀಕರಣ ವಿನಂತಿಯನ್ನು ಯಶಸ್ವಿಯಾಗಿ SMS ಇರಿಸಲಾಗಿದೆ', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, "UIN ಅಪ್‌ಡೇಟ್" ಗಾಗಿ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಇರಿಸಲಾಗಿದೆ. ನಿಮ್ಮ RID (Req ಸಂಖ್ಯೆ) ಟ್ರ್ಯಾಕಿಂಗ್‌ಗಾಗಿ $RID ಆಗಿದೆ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_UIN_UPDATE_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1207' AND lang_code='kan';
UPDATE master.template
SET "name"='تم تقديم طلب تحديث UIN بنجاح عبر الرسائل القصيرة', descr='تم تقديم طلب تحديث UIN بنجاح عبر الرسائل القصيرة', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، تم تقديم طلبك لـ "تحديث UIN" بنجاح. RID الخاص بك (رقم الطلب) هو $ RID للتتبع. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_UIN_UPDATE_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1207' AND lang_code='ara';
UPDATE master.template
SET "name"='E-mail d''échec de la demande de mise à jour UIN', descr='E-mail d''échec de la demande de mise à jour UIN', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Nous n''avons pas pu traiter votre demande de «mise à jour UIN». Veuillez réessayer plus tard. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_UPDATE_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1208' AND lang_code='fra';
UPDATE master.template
SET "name"='UIN ನವೀಕರಣ ವಿನಂತಿ ವಿಫಲವಾದ ಇಮೇಲ್', descr='UIN ನವೀಕರಣ ವಿನಂತಿ ವಿಫಲವಾದ ಇಮೇಲ್', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ನಿಮ್ಮ "UIN ಅಪ್‌ಡೇಟ್" ವಿನಂತಿಯನ್ನು ಪ್ರಕ್ರಿಯೆಗೊಳಿಸಲು ನಮಗೆ ಸಾಧ್ಯವಾಗಲಿಲ್ಲ. ದಯವಿಟ್ಟು ನಂತರ ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_UIN_UPDATE_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1208' AND lang_code='kan';
UPDATE master.template
SET "name"='فشل طلب تحديث UIN عبر البريد الإلكتروني', descr='فشل طلب تحديث UIN عبر البريد الإلكتروني', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، لم نتمكن من معالجة طلب "تحديث UIN" الخاص بك. الرجاء معاودة المحاولة في وقت لاحق. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_UIN_UPDATE_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1208' AND lang_code='ara';
UPDATE master.template
SET "name"='UIN புதுப்பிப்பு கோரிக்கை மின்னஞ்சல் தோல்வி', descr='UIN புதுப்பிப்பு கோரிக்கை மின்னஞ்சல் தோல்வி', file_format_code='txt', model='velocity', file_txt='ஹாய் $ fullName_eng, உங்கள் "UIN புதுப்பிப்பு" கோரிக்கையை எங்களால் செயல்படுத்த முடியவில்லை. பிறகு முயற்சிக்கவும். நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_UIN_UPDATE_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1208' AND lang_code='tam';
UPDATE master.template
SET "name"='UIN Update Request Failed Email', descr='UIN Update Request Failed Email', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, We were unable to process your "UIN Update" request. Please try again later. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_UPDATE_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1208' AND lang_code='eng';
UPDATE master.template
SET "name"='यूआईएन अपडेट अनुरोध ईमेल विफल', descr='यूआईएन अपडेट अनुरोध ईमेल विफल', file_format_code='txt', model='velocity', file_txt='नमस्ते $ Fullaname_Enga, हम आपके "Uin अपडेट" अनुरोध को संसाधित करने में सक्षम थे। कृपया पुन: प्रयास करें। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_UIN_UPDATE_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1208' AND lang_code='hin';
UPDATE master.template
SET "name"='Échec de la demande de mise à jour de l''UIN', descr='Échec de la demande de mise à jour de l''UIN', file_format_code='txt', model='velocity', file_txt='Échec de la demande de mise à jour UIN', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_UPDATE_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1209' AND lang_code='fra';
UPDATE master.template
SET "name"='UIN ಅಪ್‌ಡೇಟ್ ವಿನಂತಿ ವಿಫಲವಾದ ಇಮೇಲ್ ವಿಷಯ', descr='UIN ಅಪ್‌ಡೇಟ್ ವಿನಂತಿ ವಿಫಲವಾದ ಇಮೇಲ್ ವಿಷಯ', file_format_code='txt', model='velocity', file_txt='UIN ನವೀಕರಣ ವಿನಂತಿ ವಿಫಲವಾಗಿದೆ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_UIN_UPDATE_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1209' AND lang_code='kan';
UPDATE master.template
SET "name"='UIN புதுப்பிப்பு கோரிக்கையில் தோல்வியடைந்த மின்னஞ்சல் உள்ளடக்கம்', descr='UIN புதுப்பிப்பு கோரிக்கையில் தோல்வியடைந்த மின்னஞ்சல் உள்ளடக்கம்', file_format_code='txt', model='velocity', file_txt='UIN புதுப்பிப்பு கோரிக்கை தோல்வியடைந்தது', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_UIN_UPDATE_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1209' AND lang_code='tam';
UPDATE master.template
SET "name"='ईमेल सामग्री जो यूआईएन अपडेट अनुरोध में विफल रही', descr='ईमेल सामग्री जो यूआईएन अपडेट अनुरोध में विफल रही', file_format_code='txt', model='velocity', file_txt='यूआईएन अपडेट अनुरोध विफल', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_UIN_UPDATE_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1209' AND lang_code='hin';
UPDATE master.template
SET "name"='UIN Update Request Failed Email Subject', descr='UIN Update Request Failed Email Subject', file_format_code='txt', model='velocity', file_txt='UIN Update Request Failed', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_UPDATE_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1209' AND lang_code='eng';
UPDATE master.template
SET "name"='فشل طلب تحديث UIN عنوان البريد الإلكتروني', descr='فشل طلب تحديث UIN عنوان البريد الإلكتروني', file_format_code='txt', model='velocity', file_txt='فشل طلب تحديث UIN', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_UIN_UPDATE_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1209' AND lang_code='ara';
UPDATE master.template
SET "name"='فشل طلب تحديث UIN عبر الرسائل القصيرة', descr='فشل طلب تحديث UIN عبر الرسائل القصيرة', file_format_code='txt', model='velocity', file_txt='مرحبًا $ fullName_eng ، لم نتمكن من معالجة طلب "تحديث UIN" الخاص بك. الرجاء معاودة المحاولة في وقت لاحق. شكرا لكم!', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_UIN_UPDATE_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1210' AND lang_code='ara';
UPDATE master.template
SET "name"='UIN ನವೀಕರಣ ವಿನಂತಿ ವಿಫಲವಾದ SMS', descr='UIN ನವೀಕರಣ ವಿನಂತಿ ವಿಫಲವಾದ SMS', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $fullName_eng, ನಿಮ್ಮ "UIN ಅಪ್‌ಡೇಟ್" ವಿನಂತಿಯನ್ನು ಪ್ರಕ್ರಿಯೆಗೊಳಿಸಲು ನಮಗೆ ಸಾಧ್ಯವಾಗಲಿಲ್ಲ. ದಯವಿಟ್ಟು ನಂತರ ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ. ಧನ್ಯವಾದಗಳು!', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_UIN_UPDATE_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1210' AND lang_code='kan';
UPDATE master.template
SET "name"='UIN Update Request Failed SMS', descr='UIN Update Request Failed SMS', file_format_code='txt', model='velocity', file_txt='Hi $fullName_eng, We were unable to process your "UIN Update" request. Please try again later. Thank You!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_UPDATE_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1210' AND lang_code='eng';
UPDATE master.template
SET "name"='यूआईएन अपडेट अनुरोध एसएमएस विफल', descr='यूआईएन अपडेट अनुरोध एसएमएस विफल', file_format_code='txt', model='velocity', file_txt='नमस्ते $ fullName_eng, हम आपके "यूआईएन अपडेट" अनुरोध को संसाधित करने में असमर्थ हैं। बाद में पुन: प्रयास करें। धन्यवाद!', module_id='10006', module_name='घरेलू सेवाएं', template_typ_code='RS_UIN_UPDATE_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1210' AND lang_code='hin';
UPDATE master.template
SET "name"='Echec de la demande de mise à jour UIN SMS', descr='Echec de la demande de mise à jour UIN SMS', file_format_code='txt', model='velocity', file_txt='Bonjour $fullName_fra, Nous n''avons pas pu traiter votre demande de «mise à jour UIN». Veuillez réessayer plus tard. Merci!', module_id='10006', module_name='Resident Services', template_typ_code='RS_UIN_UPDATE_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1210' AND lang_code='fra';
UPDATE master.template
SET "name"='UIN புதுப்பிப்பு கோரிக்கை SMS தோல்வியடைந்தது', descr='UIN புதுப்பிப்பு கோரிக்கை SMS தோல்வியடைந்தது', file_format_code='txt', model='velocity', file_txt='ஹாய் $ fullName_eng, உங்கள் "UIN புதுப்பிப்பு" கோரிக்கையை எங்களால் செயல்படுத்த முடியவில்லை. பிறகு முயற்சிக்கவும். நன்றி!', module_id='10006', module_name=' வீட்டு சேவைகள்', template_typ_code='RS_UIN_UPDATE_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1210' AND lang_code='tam';
UPDATE master.template
SET "name"='நற்சான்றிதழ் வழங்கல் வெற்றி குறித்து SMS', descr='நற்சான்றிதழ் வழங்கல் வெற்றி குறித்து SMS', file_format_code='txt', model='velocity', file_txt='ஹாய் $! FullName, $! PartnerName இலிருந்து $! CredentialName க்கான கோரிக்கையைப் பெற்றுள்ளோம். கோரிக்கை ஐடி $! RID மற்றும் உங்கள் குறியாக்க விசை $! EncryptionKey. இந்த கோரிக்கை செயல்பாட்டில் உள்ளது. நன்றி', module_id='10006', module_name='வீட்டு சேவைகள்', template_typ_code='RS_CRE_REQ_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1232' AND lang_code='tam';
UPDATE master.template
SET "name"='Credential Issuance Success SMS', descr='Credential Issuance Success SMS', file_format_code='txt', model='velocity', file_txt='Hi $!fullName, We have received a request for $!credentialName from $!partnerName. The request id for the same is $!RID and your encryption key is $!encryptionKey. This request is under processing. Thank You', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_REQ_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1232' AND lang_code='eng';
UPDATE master.template
SET "name"='الرسائل القصيرة الناجحة لإصدار الاعتماد', descr='الرسائل القصيرة الناجحة لإصدار الاعتماد', file_format_code='txt', model='velocity', file_txt='مرحبًا $! fullName ، لقد تلقينا طلبًا للحصول على $! creditName من $! partnerName. معرف الطلب لنفسه هو $! RID ومفتاح التشفير الخاص بك هو $! encryptionKey. هذا الطلب قيد المعالجة. شكرا لكم', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_CRE_REQ_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1232' AND lang_code='ara';
UPDATE master.template
SET "name"='ರುಜುವಾತು ವಿತರಣೆಯ ಯಶಸ್ಸಿನ SMS ', descr='ರುಜುವಾತು ವಿತರಣೆಯ ಯಶಸ್ಸಿನ SMS', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $!fullName, ನಾವು $!partnerName ನಿಂದ $!credentialName ಗಾಗಿ ವಿನಂತಿಯನ್ನು ಸ್ವೀಕರಿಸಿದ್ದೇವೆ. ವಿನಂತಿ ಐಡಿ $!RID ಮತ್ತು ನಿಮ್ಮದು ಎನ್‌ಕ್ರಿಪ್ಶನ್ ಕೀ $!encryptionKey. ಈ ವಿನಂತಿಯು ಪ್ರಕ್ರಿಯೆಯಲ್ಲಿದೆ. ಧನ್ಯವಾದಗಳು', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_CRE_REQ_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1232' AND lang_code='kan';
UPDATE master.template
SET "name"='क्रेडेंशियल डिलीवरी की सफलता पर एसएमएस ', descr='क्रेडेंशियल डिलीवरी की सफलता पर एसएमएस', file_format_code='txt', model='velocity', file_txt='नमस्ते $! FullName, हमें $! PartnerName से $! CredentialName के लिए एक अनुरोध प्राप्त हुआ है। अनुरोध आईडी $! RID और आपकी एन्क्रिप्शन कुंजी $! EncryptionKey। यह अनुरोध प्रक्रिया में है। शुक्रिया', module_id='10006', module_name=' घरेलू सेवाएं', template_typ_code='RS_CRE_REQ_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1232' AND lang_code='hin';
UPDATE master.template
SET "name"='Credential Issuance Success SMS', descr='Credential Issuance Success SMS', file_format_code='txt', model='velocity', file_txt='Bonjour $!fullName, Nous avons reçu une demande de $!credentialName de $!partnerName. L''identifiant de la demande est $!RID et votre clé de chiffrement est $!encryptionKey. Cette demande est en cours de traitement. Merci', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_REQ_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1232' AND lang_code='fra';
UPDATE master.template
SET "name"='Credential Issuance Success EMAIL', descr='Credential Issuance Success EMAIL', file_format_code='txt', model='velocity', file_txt='Bonjour $!fullName, Nous avons reçu une demande de $!credentialName de $!partnerName. L''identifiant de la demande est $!RID et votre clé de chiffrement est $!encryptionKey. Cette demande est en cours de traitement. Merci', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_REQ_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1233' AND lang_code='fra';
UPDATE master.template
SET "name"='நற்சான்றிதழ் வழங்கல் EMAIL வெற்றி', descr='நற்சான்றிதழ் வழங்கல் EMAIL வெற்றி', file_format_code='txt', model='velocity', file_txt='ஹாய் $! FullName, $! PartnerName இலிருந்து $! CredentialName க்கான கோரிக்கையைப் பெற்றுள்ளோம். இதற்கான கோரிக்கை ஐடி $! RID மற்றும் உங்கள் குறியாக்க விசை $! EncryptionKey. இந்தக் கோரிக்கை செயலாக்கத்தில் உள்ளது. நன்றி ', module_id='10006', module_name='வீட்டு சேவைகள்', template_typ_code='RS_CRE_REQ_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1233' AND lang_code='tam';
UPDATE master.template
SET "name"='ರುಜುವಾತು ವಿತರಣೆಯ ಯಶಸ್ಸು EMAIL', descr='ರುಜುವಾತು ವಿತರಣೆಯ ಯಶಸ್ಸು EMAIL', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $!fullName, ನಾವು $!partnerName ನಿಂದ $!credentialName ಗಾಗಿ ವಿನಂತಿಯನ್ನು ಸ್ವೀಕರಿಸಿದ್ದೇವೆ. ಇದಕ್ಕಾಗಿ ವಿನಂತಿ ಐಡಿ $!RID ಮತ್ತು ನಿಮ್ಮದು encryption key is $!encryptionKey. This request is under processing. Thank You', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_CRE_REQ_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1233' AND lang_code='kan';
UPDATE master.template
SET "name"='क्रेडेंशियल वितरण EMAIL की सफलता', descr='क्रेडेंशियल वितरण EMAIL की सफलता', file_format_code='txt', model='velocity', file_txt='नमस्ते $! FullName, हमें $! PartnerName से $! CredentialName के लिए एक अनुरोध प्राप्त हुआ है। इसके लिए अनुरोध आईडी $! RID है और आपकी एन्क्रिप्शन कुंजी $! EncryptionKey है। यह अनुरोध संसाधित किया जा रहा है। शुक्रिया', module_id='10006', module_name=' घरेलू सेवाएं', template_typ_code='RS_CRE_REQ_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1233' AND lang_code='hin';
UPDATE master.template
SET "name"='Credential Issuance Success EMAIL', descr='Credential Issuance Success EMAIL', file_format_code='txt', model='velocity', file_txt='Hi $!fullName, We have received a request for $!credentialName from $!partnerName. The request id for the same is $!RID and your encryption key is $!encryptionKey. This request is under processing. Thank You', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_REQ_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1233' AND lang_code='eng';
UPDATE master.template
SET "name"='نجاح إصدار بيانات الاعتماد EMAIL', descr='نجاح إصدار بيانات الاعتماد EMAIL', file_format_code='txt', model='velocity', file_txt='مرحبًا $! fullName ، لقد تلقينا طلبًا للحصول على $! creditName من $! partnerName. معرف الطلب لنفسه هو $! RID ومفتاح التشفير الخاص بك هو $! encryptionKey. هذا الطلب قيد المعالجة. شكرا لكم', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_CRE_REQ_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1233' AND lang_code='ara';
UPDATE master.template
SET "name"='Credential Issuance Success EMAIL Subject', descr='Credential Issuance Success EMAIL Subject', file_format_code='txt', model='velocity', file_txt='Confirmation de la délivrance des informations d''identification', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_REQ_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1234' AND lang_code='fra';
UPDATE master.template
SET "name"='ರುಜುವಾತು ವಿತರಣೆಯ ಯಶಸ್ಸು ಇಮೇಲ್ ವಿಷಯ', descr='ರುಜುವಾತು ವಿತರಣೆಯ ಯಶಸ್ಸು ಇಮೇಲ್ ವಿಷಯ', file_format_code='txt', model='velocity', file_txt='ರುಜುವಾತು ನೀಡಿಕೆ ದೃಢೀಕರಣ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_CRE_REQ_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1234' AND lang_code='kan';
UPDATE master.template
SET "name"='موضوع EMAIL بنجاح إصدار بيانات الاعتماد', descr='موضوع EMAIL بنجاح إصدار بيانات الاعتماد', file_format_code='txt', model='velocity', file_txt='تأكيد إصدار الاعتماد', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_CRE_REQ_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1234' AND lang_code='ara';
UPDATE master.template
SET "name"='Credential Issuance Success EMAIL Subject', descr='Credential Issuance Success EMAIL Subject', file_format_code='txt', model='velocity', file_txt='Credential Issuance Confirmation', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_REQ_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1234' AND lang_code='eng';
UPDATE master.template
SET "name"='क्रेडेंशियल वितरण की सफलता ईमेल का विषय है', descr='क्रेडेंशियल वितरण की सफलता ईमेल का विषय है', file_format_code='txt', model='velocity', file_txt='क्रेडेंशियल का प्रमाणीकरण', module_id='10006', module_name=' घरेलू सेवाएं', template_typ_code='RS_CRE_REQ_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1234' AND lang_code='hin';
UPDATE master.template
SET "name"='நற்சான்றிதழ் விநியோகத்தின் வெற்றி என்பது மின்னஞ்சலின் பொருள்', descr='நற்சான்றிதழ் விநியோகத்தின் வெற்றி என்பது மின்னஞ்சலின் பொருள்', file_format_code='txt', model='velocity', file_txt='நற்சான்றிதழின் அங்கீகாரம்', module_id='10006', module_name='வீட்டு சேவைகள்', template_typ_code='RS_CRE_REQ_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1234' AND lang_code='tam';
UPDATE master.template
SET "name"='சான்று விநியோக நிலை சரிபார்ப்பு SMS', descr='சான்று விநியோக நிலை சரிபார்ப்பு SMS', file_format_code='txt', model='velocity', file_txt='ஹாய் $!fullName, கோரிக்கை ஐடி $!RIDக்கு எதிராக நற்சான்றிதழ்களை வழங்குவதற்கான உங்கள் கோரிக்கையின் நிலை $!status. நன்றி', module_id='10006', module_name='வீட்டு சேவைகள்', template_typ_code='RS_CRE_STATUS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1235' AND lang_code='tam';
UPDATE master.template
SET "name"='ರುಜುವಾತು ವಿತರಣೆ ಸ್ಥಿತಿ ಪರಿಶೀಲನೆ SMS', descr='ರುಜುವಾತು ವಿತರಣೆ ಸ್ಥಿತಿ ಪರಿಶೀಲನೆ SMS', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $!fullName, ವಿನಂತಿಯ ID $!RID ಗೆ ರುಜುವಾತುಗಳನ್ನು ನೀಡಲು ನಿಮ್ಮ ವಿನಂತಿಯ ಸ್ಥಿತಿಯು $! ಸ್ಥಿತಿಯಾಗಿದೆ. ಧನ್ಯವಾದಗಳು', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_CRE_STATUS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1235' AND lang_code='kan';
UPDATE master.template
SET "name"='प्रशंसापत्र वितरण स्थिति सत्यापन एसएमएस', descr='प्रशंसापत्र वितरण स्थिति सत्यापन एसएमएस', file_format_code='txt', model='velocity', file_txt='नमस्ते $! FullName, अनुरोध आईडी $! $! को क्रेडेंशियल देने के आपके अनुरोध की स्थिति! शर्त है। शुक्रिया', module_id='10006', module_name=' घरेलू सेवाएं', template_typ_code='RS_CRE_STATUS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1235' AND lang_code='hin';
UPDATE master.template
SET "name"='Credential Issuance Status Check SMS', descr='Credential Issuance Status Check SMS', file_format_code='txt', model='velocity', file_txt='Hi $!fullName, The status of your request to issue credentials against request ID $!RID is $!status. Thank You', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_STATUS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1235' AND lang_code='eng';
UPDATE master.template
SET "name"='Credential Issuance Status Check SMS', descr='Credential Issuance Status Check SMS', file_format_code='txt', model='velocity', file_txt='Bonjour $!fullName, L''état de votre demande d''émission d''informations d''identification par rapport à l''ID de demande $!RID est $! status. Merci', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_STATUS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1235' AND lang_code='fra';
UPDATE master.template
SET "name"='التحقق من حالة إصدار الاعتماد SMS', descr='التحقق من حالة إصدار الاعتماد SMS', file_format_code='txt', model='velocity', file_txt='مرحبًا $! fullName ، حالة طلبك لإصدار بيانات الاعتماد مقابل معرّف الطلب $! RID هي حالة $!. شكرا لكم', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_CRE_STATUS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1235' AND lang_code='ara';
UPDATE master.template
SET "name"='क्रेडेंशियल वितरण EMAIL की स्थिति जांचें', descr='क्रेडेंशियल वितरण EMAIL की स्थिति जांचें', file_format_code='txt', model='velocity', file_txt='नमस्ते $! FullName, अनुरोध आईडी $! $! को क्रेडेंशियल देने के आपके अनुरोध की स्थिति! शर्त है। शुक्रिया', module_id='10006', module_name=' घरेलू सेवाएं', template_typ_code='RS_CRE_STATUS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1236' AND lang_code='hin';
UPDATE master.template
SET "name"='Credential Issuance Status Check EMAIL', descr='Credential Issuance Status Check EMAIL', file_format_code='txt', model='velocity', file_txt='Hi $!fullName, The status of your request to issue credentials against request ID $!RID is $!status. Thank You', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_STATUS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1236' AND lang_code='eng';
UPDATE master.template
SET "name"='ರುಜುವಾತು ವಿತರಣೆಯ ಸ್ಥಿತಿ ಪರಿಶೀಲಿಸಿ EMAIL', descr='ರುಜುವಾತು ವಿತರಣೆಯ ಸ್ಥಿತಿ ಪರಿಶೀಲಿಸಿ EMAIL', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $!fullName, ವಿನಂತಿಯ ID $!RID ಗೆ ರುಜುವಾತುಗಳನ್ನು ನೀಡಲು ನಿಮ್ಮ ವಿನಂತಿಯ ಸ್ಥಿತಿಯು $! ಸ್ಥಿತಿಯಾಗಿದೆ. ಧನ್ಯವಾದಗಳು', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_CRE_STATUS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1236' AND lang_code='kan';
UPDATE master.template
SET "name"='التحقق من حالة إصدار الاعتماد EMAIL', descr='التحقق من حالة إصدار الاعتماد EMAIL', file_format_code='txt', model='velocity', file_txt='مرحبًا $! fullName ، حالة طلبك لإصدار بيانات الاعتماد مقابل معرّف الطلب $! RID هي حالة $!. شكرا لكم', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_CRE_STATUS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1236' AND lang_code='ara';
UPDATE master.template
SET "name"='நற்சான்றிதழ் விநியோகம் EMAIL இன் நிலையைச் சரிபார்க்கவும்', descr='நற்சான்றிதழ் விநியோகம் EMAIL இன் நிலையைச் சரிபார்க்கவும்', file_format_code='txt', model='velocity', file_txt='ஹாய் $!fullName, கோரிக்கை ஐடி $!RIDக்கு எதிராக நற்சான்றிதழ்களை வழங்குவதற்கான உங்கள் கோரிக்கையின் நிலை $!status. நன்றி', module_id='10006', module_name='வீட்டு சேவைகள்', template_typ_code='RS_CRE_STATUS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1236' AND lang_code='tam';
UPDATE master.template
SET "name"='Credential Issuance Status Check EMAIL', descr='Credential Issuance Status Check EMAIL', file_format_code='txt', model='velocity', file_txt='Bonjour $!fullName, L''état de votre demande d''émission d''informations d''identification par rapport à l''ID de demande $!RID est $! status. Merci', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_STATUS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1236' AND lang_code='fra';
UPDATE master.template
SET "name"='वितरण वितरण स्थिति सत्यापित करें EMAIL सामग्री', descr='वितरण वितरण स्थिति सत्यापित करें EMAIL सामग्री', file_format_code='txt', model='velocity', file_txt='क्रेडेंशियल वितरण की स्थिति', module_id='10006', module_name=' घरेलू सेवाएं', template_typ_code='RS_CRE_STATUS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1237' AND lang_code='hin';
UPDATE master.template
SET "name"='டெலிவரி விநியோக நிலை மின்னஞ்சல் உள்ளடக்கத்தைச் சரிபார்க்கவும்', descr='டெலிவரி விநியோக நிலை மின்னஞ்சல் உள்ளடக்கத்தைச் சரிபார்க்கவும்', file_format_code='txt', model='velocity', file_txt='நற்சான்றிதழ் விநியோகத்தின் நிலை', module_id='10006', module_name='வீட்டு சேவைகள்', template_typ_code='RS_CRE_STATUS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1237' AND lang_code='tam';
UPDATE master.template
SET "name"='Credential Issuance Status Check EMAIL Subject', descr='Credential Issuance Status Check EMAIL Subject', file_format_code='txt', model='velocity', file_txt='Credential Issuance Status', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_STATUS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1237' AND lang_code='eng';
UPDATE master.template
SET "name"='Credential Issuance Status Check EMAIL Subject', descr='Credential Issuance Status Check EMAIL Subject', file_format_code='txt', model='velocity', file_txt='État de délivrance des informations d''identification', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_STATUS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1237' AND lang_code='fra';
UPDATE master.template
SET "name"='ರುಜುವಾತು ವಿತರಣೆ ಸ್ಥಿತಿ EMAIL ವಿಷಯ ಪರಿಶೀಲಿಸಿ', descr='ರುಜುವಾತು ವಿತರಣೆ ಸ್ಥಿತಿ EMAIL ವಿಷಯ ಪರಿಶೀಲಿಸಿ', file_format_code='txt', model='velocity', file_txt='ರುಜುವಾತು ವಿತರಣೆಯ ಸ್ಥಿತಿ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_CRE_STATUS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1237' AND lang_code='kan';
UPDATE master.template
SET "name"='التحقق من حالة إصدار الاعتماد EMAIL الموضوع', descr='التحقق من حالة إصدار الاعتماد EMAIL الموضوع', file_format_code='txt', model='velocity', file_txt='حالة إصدار الاعتماد', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_CRE_STATUS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1237' AND lang_code='ara';
UPDATE master.template
SET "name"='एक क्रेडेंशियल अनुरोध रद्द करें सफल एसएमएस', descr='एक क्रेडेंशियल अनुरोध रद्द करें सफल एसएमएस', file_format_code='txt', model='velocity', file_txt='नमस्ते $! FullName, अनुरोध आईडी $! RID प्लस आपका क्रेडेंशियल अनुरोध सफलतापूर्वक रद्द कर दिया गया। शुक्रिया', module_id='10006', module_name=' घरेलू सेवाएं', template_typ_code='RS_CRE_CANCEL_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1238' AND lang_code='hin';
UPDATE master.template
SET "name"='ರುಜುವಾತು ವಿನಂತಿಯನ್ನು ರದ್ದುಗೊಳಿಸಿ ಯಶಸ್ವಿ SMS', descr='ರುಜುವಾತು ವಿನಂತಿಯನ್ನು ರದ್ದುಗೊಳಿಸಿ ಯಶಸ್ವಿ SMS', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $!fullName, ವಿನಂತಿ ಐಡಿ $!RID ಜೊತೆಗೆ ನಿಮ್ಮ ರುಜುವಾತು ವಿನಂತಿಯನ್ನು ಯಶಸ್ವಿಯಾಗಿ ರದ್ದುಗೊಳಿಸಲಾಗಿದೆ. ಧನ್ಯವಾದಗಳು', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_CRE_CANCEL_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1238' AND lang_code='kan';
UPDATE master.template
SET "name"='طلب الاعتماد إلغاء الرسائل القصيرة الناجحة', descr='طلب الاعتماد إلغاء الرسائل القصيرة الناجحة', file_format_code='txt', model='velocity', file_txt='مرحبًا $! fullName ، طلب الاعتماد الخاص بك مع معرف الطلب $! تم إلغاء RID بنجاح. شكرا لكم', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_CRE_CANCEL_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1238' AND lang_code='ara';
UPDATE master.template
SET "name"='Credential Request Cancel Success SMS', descr='Credential Request Cancel Success SMS', file_format_code='txt', model='velocity', file_txt='Salut $!fullName, Votre demande d''informations d''identification avec l''ID de demande $!RID a été annulée avec succès. Merci', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_CANCEL_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1238' AND lang_code='fra';
UPDATE master.template
SET "name"='Credential Request Cancel Success SMS', descr='Credential Request Cancel Success SMS', file_format_code='txt', model='velocity', file_txt='Hi $!fullName, Your credential request with request id $!RID has been successfully cancelled. Thank You', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_CANCEL_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1238' AND lang_code='eng';
UPDATE master.template
SET "name"='நற்சான்றிதழ் கோரிக்கையை ரத்துசெய் வெற்றிகரமான SMS', descr='நற்சான்றிதழ் கோரிக்கையை ரத்துசெய் வெற்றிகரமான SMS', file_format_code='txt', model='velocity', file_txt='வணக்கம் $!fullName, கோரிக்கை ஐடியுடன் கூடிய உங்கள் நற்சான்றிதழ் கோரிக்கை $!RID வெற்றிகரமாக ரத்துசெய்யப்பட்டது. நன்றி', module_id='10006', module_name='வீட்டு சேவைகள்', template_typ_code='RS_CRE_CANCEL_SUCCESS_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1238' AND lang_code='tam';
UPDATE master.template
SET "name"='Credential Request Cancel Success EMAIL', descr='Credential Request Cancel Success EMAIL', file_format_code='txt', model='velocity', file_txt='Hi $!fullName, Your credential request with request id $RID has been successfully cancelled. Thank You', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_CANCEL_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1239' AND lang_code='eng';
UPDATE master.template
SET "name"='ರುಜುವಾತು ವಿನಂತಿಯನ್ನು ರದ್ದುಗೊಳಿಸಿ ಯಶಸ್ಸು EMAIL', descr='ರುಜುವಾತು ವಿನಂತಿಯನ್ನು ರದ್ದುಗೊಳಿಸಿ ಯಶಸ್ಸು EMAIL', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $!fullName, $RID ವಿನಂತಿ ಐಡಿಯೊಂದಿಗೆ ನಿಮ್ಮ ರುಜುವಾತು ವಿನಂತಿಯನ್ನು ಯಶಸ್ವಿಯಾಗಿ ರದ್ದುಗೊಳಿಸಲಾಗಿದೆ. ಧನ್ಯವಾದಗಳು', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_CRE_CANCEL_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1239' AND lang_code='kan';
UPDATE master.template
SET "name"='Credential Request Cancel Success EMAIL', descr='Credential Request Cancel Success EMAIL', file_format_code='txt', model='velocity', file_txt='Salut $!fullName, Votre demande d''informations d''identification avec l''ID de demande $!RID a été annulée avec succès. Merci', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_CANCEL_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1239' AND lang_code='fra';
UPDATE master.template
SET "name"='طلب الاعتماد إلغاء النجاح EMAIL', descr='طلب الاعتماد إلغاء النجاح EMAIL', file_format_code='txt', model='velocity', file_txt='مرحبًا $! fullName ، تم بنجاح إلغاء طلب الاعتماد الخاص بك مع معرف الطلب $ RID. شكرا لكم', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_CRE_CANCEL_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1239' AND lang_code='ara';
UPDATE master.template
SET "name"='நற்சான்றிதழ் கோரிக்கையை ரத்து செய் வெற்றி EMAIL', descr='நற்சான்றிதழ் கோரிக்கையை ரத்து செய் வெற்றி EMAIL', file_format_code='txt', model='velocity', file_txt='ஹாய் $! FullName, $ RID கோரிக்கை ஐடியுடன் கூடிய உங்கள் நற்சான்றிதழ் கோரிக்கை வெற்றிகரமாக ரத்து செய்யப்பட்டது. நன்றி', module_id='10006', module_name='வீட்டு சேவைகள்', template_typ_code='RS_CRE_CANCEL_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1239' AND lang_code='tam';
UPDATE master.template
SET "name"='एक क्रेडेंशियल अनुरोध रद्द करें सफल ईमेल', descr='एक क्रेडेंशियल अनुरोध रद्द करें सफल ईमेल', file_format_code='txt', model='velocity', file_txt='नमस्ते $! FullName, $ RID अनुरोध आईडी के साथ आपका क्रेडेंशियल अनुरोध सफलतापूर्वक रद्द कर दिया गया है। शुक्रिया', module_id='10006', module_name=' घरेलू सेवाएं', template_typ_code='RS_CRE_CANCEL_SUCCESS_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1239' AND lang_code='hin';
UPDATE master.template
SET "name"='طلب اعتماد إلغاء بنجاح موضوع EMAIL', descr='طلب اعتماد إلغاء بنجاح موضوع EMAIL', file_format_code='txt', model='velocity', file_txt='تم إلغاء طلب الاعتماد بنجاح', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_CRE_CANCEL_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1240' AND lang_code='ara';
UPDATE master.template
SET "name"='एक क्रेडेंशियल अनुरोध रद्द करें सफलता ईमेल विषय', descr='एक क्रेडेंशियल अनुरोध रद्द करें सफलता ईमेल विषय', file_format_code='txt', model='velocity', file_txt='क्रेडेंशियल अनुरोध सफलतापूर्वक रद्द कर दिया गया था', module_id='10006', module_name=' घरेलू सेवाएं', template_typ_code='RS_CRE_CANCEL_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1240' AND lang_code='hin';
UPDATE master.template
SET "name"='நற்சான்றிதழ் கோரிக்கையை ரத்துசெய் வெற்றி மின்னஞ்சல் பொருள்', descr='நற்சான்றிதழ் கோரிக்கையை ரத்துசெய் வெற்றி மின்னஞ்சல் பொருள்', file_format_code='txt', model='velocity', file_txt='நற்சான்றிதழ் கோரிக்கை வெற்றிகரமாக ரத்து செய்யப்பட்டது', module_id='10006', module_name='வீட்டு சேவைகள்', template_typ_code='RS_CRE_CANCEL_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1240' AND lang_code='tam';
UPDATE master.template
SET "name"='ರುಜುವಾತು ವಿನಂತಿಯನ್ನು ರದ್ದುಗೊಳಿಸು ಯಶಸ್ಸಿನ ಇಮೇಲ್ ವಿಷಯ', descr='ರುಜುವಾತು ವಿನಂತಿಯನ್ನು ರದ್ದುಗೊಳಿಸು ಯಶಸ್ಸಿನ ಇಮೇಲ್ ವಿಷಯ', file_format_code='txt', model='velocity', file_txt='ರುಜುವಾತು ವಿನಂತಿಯನ್ನು ಯಶಸ್ವಿಯಾಗಿ ರದ್ದುಗೊಳಿಸಲಾಗಿದೆ ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_CRE_CANCEL_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1240' AND lang_code='kan';
UPDATE master.template
SET "name"='Credential Request Cancel Success EMAIL Subject', descr='Credential Request Cancel Success EMAIL Subject', file_format_code='txt', model='velocity', file_txt='Credential Request is cancelled successfully', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_CANCEL_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1240' AND lang_code='eng';
UPDATE master.template
SET "name"='Credential Request Cancel Success EMAIL Subject', descr='Credential Request Cancel Success EMAIL Subject', file_format_code='txt', model='velocity', file_txt='La demande d''informations d''identification est annulée avec succès', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_CANCEL_SUCCESS_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1240' AND lang_code='fra';
UPDATE master.template
SET "name"='क्रेडेंशियल वितरण विफलता एसएमएस', descr='क्रेडेंशियल वितरण विफलता एसएमएस', file_format_code='txt', model='velocity', file_txt='नमस्ते $! FullName, हमें $! PartnerName से $! CredentialName के लिए एक अनुरोध प्राप्त हुआ है। इसके लिए अनुरोध आईडी $! RID है। अनुरोध संसाधित करने में असमर्थ. बाद में पुन: प्रयास करें। शुक्रिया', module_id='10006', module_name=' घरेलू सेवाएं', template_typ_code='RS_CRE_REQ_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1241' AND lang_code='hin';
UPDATE master.template
SET "name"='Credential Issuance Failure SMS', descr='Credential Issuance Failure SMS', file_format_code='txt', model='velocity', file_txt='Hi $!fullName, We have received a request for $!credentialName from $!partnerName. The request id for the same is $!RID. The request could not be processed. Please try again later. Thank You', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_REQ_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1241' AND lang_code='eng';
UPDATE master.template
SET "name"='Credential Issuance Failure SMS', descr='Credential Issuance Failure SMS', file_format_code='txt', model='velocity', file_txt='Bonjour $!fullName, Nous avons reçu une demande de $!credentialName de $!partnerName. L''identifiant de demande pour le même est $!RID. La demande n''a pas pu être traitée. Veuillez réessayer plus tard. Merci', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_REQ_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1241' AND lang_code='fra';
UPDATE master.template
SET "name"='رسالة فشل في إصدار بيانات الاعتماد', descr='رسالة فشل في إصدار بيانات الاعتماد', file_format_code='txt', model='velocity', file_txt='مرحبًا $! fullName ، لقد تلقينا طلبًا للحصول على $! creditName من $! partnerName. معرف الطلب لنفسه هو $! RID. لا يمكن معالجة الطلب. الرجاء معاودة المحاولة في وقت لاحق. شكرا لكم', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_CRE_REQ_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1241' AND lang_code='ara';
UPDATE master.template
SET "name"='நற்சான்றிதழ் விநியோகம் தோல்வி SMS', descr='நற்சான்றிதழ் விநியோகம் தோல்வி SMS', file_format_code='txt', model='velocity', file_txt='ஹாய் $! FullName, $! PartnerName இலிருந்து $! CredentialName க்கான கோரிக்கையைப் பெற்றுள்ளோம். இதற்கான கோரிக்கை ஐடி $! RID. கோரிக்கையைச் செயல்படுத்த முடியவில்லை. பிறகு முயற்சிக்கவும். நன்றி ', module_id='10006', module_name='வீட்டு சேவைகள்', template_typ_code='RS_CRE_REQ_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1241' AND lang_code='tam';
UPDATE master.template
SET "name"='ರುಜುವಾತು ವಿತರಣೆ ವಿಫಲತೆ SMS', descr='ರುಜುವಾತು ವಿತರಣೆ ವಿಫಲತೆ SMS', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $!fullName, ನಾವು $!partnerName ನಿಂದ $!credentialName ಗಾಗಿ ವಿನಂತಿಯನ್ನು ಸ್ವೀಕರಿಸಿದ್ದೇವೆ. ಇದಕ್ಕಾಗಿ ವಿನಂತಿ ಐಡಿ $!RID ಆಗಿದೆ. ವಿನಂತಿಯನ್ನು ಪ್ರಕ್ರಿಯೆಗೊಳಿಸಲು ಸಾಧ್ಯವಾಗಲಿಲ್ಲ. ದಯವಿಟ್ಟು ನಂತರ ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ. ಧನ್ಯವಾದಗಳು', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_CRE_REQ_FAILURE_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1241' AND lang_code='kan';
UPDATE master.template
SET "name"='Credential Issuance Failure EMAIL', descr='Credential Issuance Failure EMAIL', file_format_code='txt', model='velocity', file_txt='Bonjour $!fullName, Nous avons reçu une demande de $!credentialName de $!partnerName. L''identifiant de demande pour le même est $!RID. La demande n''a pas pu être traitée. Veuillez réessayer plus tard. Merci', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_REQ_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1242' AND lang_code='fra';
UPDATE master.template
SET "name"='क्रेडेंशियल वितरण विफलता EMAIL', descr='क्रेडेंशियल वितरण विफलता EMAIL', file_format_code='txt', model='velocity', file_txt='नमस्ते $! FullName, हमें $! PartnerName से $! CredentialName के लिए एक अनुरोध प्राप्त हुआ है। $ अनुरोध आईडी के लिए वही $! RID है। अनुरोध संसाधित नहीं किया जा सका। बाद में पुन: प्रयास करें। शुक्रिया', module_id='10006', module_name=' घरेलू सेवाएं', template_typ_code='RS_CRE_REQ_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1242' AND lang_code='hin';
UPDATE master.template
SET "name"='فشل إصدار بيانات الاعتماد EMAIL', descr='فشل إصدار بيانات الاعتماد EMAIL', file_format_code='txt', model='velocity', file_txt='مرحبًا $! fullName ، لقد تلقينا طلبًا للحصول على $! creditName من $! partnerName. معرف الطلب لنفسه هو $! RID. لا يمكن معالجة الطلب. الرجاء معاودة المحاولة في وقت لاحق. شكرا لكم', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_CRE_REQ_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1242' AND lang_code='ara';
UPDATE master.template
SET "name"='நற்சான்றிதழ் விநியோகம் தோல்வி EMAIL', descr='நற்சான்றிதழ் விநியோகம் தோல்வி EMAIL', file_format_code='txt', model='velocity', file_txt='ஹாய் $! FullName, $! PartnerName இலிருந்து $! CredentialName க்கான கோரிக்கையைப் பெற்றுள்ளோம். இதற்கான கோரிக்கை ஐடி $! RID. கோரிக்கையைச் செயல்படுத்த முடியவில்லை. பிறகு முயற்சிக்கவும். நன்றி ', module_id='10006', module_name='வீட்டு சேவைகள்', template_typ_code='RS_CRE_REQ_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1242' AND lang_code='tam';
UPDATE master.template
SET "name"='ರುಜುವಾತು ವಿತರಣೆ ವಿಫಲತೆ EMAIL', descr='ರುಜುವಾತು ವಿತರಣೆ ವಿಫಲತೆ EMAIL', file_format_code='txt', model='velocity', file_txt='ಹಾಯ್ $!fullName, ನಾವು $!partnerName ನಿಂದ $!credentialName ಗಾಗಿ ವಿನಂತಿಯನ್ನು ಸ್ವೀಕರಿಸಿದ್ದೇವೆ. ಇದಕ್ಕಾಗಿ ವಿನಂತಿ ಐಡಿ $!RID ಆಗಿದೆ. ವಿನಂತಿಯನ್ನು ಪ್ರಕ್ರಿಯೆಗೊಳಿಸಲು ಸಾಧ್ಯವಾಗಲಿಲ್ಲ. ದಯವಿಟ್ಟು ನಂತರ ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ. ಧನ್ಯವಾದಗಳು', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_CRE_REQ_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1242' AND lang_code='kan';
UPDATE master.template
SET "name"='Credential Issuance Failure EMAIL', descr='Credential Issuance Failure EMAIL', file_format_code='txt', model='velocity', file_txt='Hi $!fullName, We have received a request for $!credentialName from $!partnerName. The request id for the same is $!RID. The request could not be processed. Please try again later. Thank You', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_REQ_FAILURE_EMAIL', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1242' AND lang_code='eng';
UPDATE master.template
SET "name"='वितरण वितरण विफलता ईमेल विषय', descr='वितरण वितरण विफलता ईमेल विषय', file_format_code='txt', model='velocity', file_txt='क्रेडेंशियल की डिलीवरी विफल', module_id='10006', module_name=' घरेलू सेवाएं', template_typ_code='RS_CRE_REQ_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1243' AND lang_code='hin';
UPDATE master.template
SET "name"='Credential Issuance Failure EMAIL Subject', descr='Credential Issuance Failure EMAIL Subject', file_format_code='txt', model='velocity', file_txt='Credential Issuance Failed', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_REQ_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1243' AND lang_code='eng';
UPDATE master.template
SET "name"='Credential Issuance Failure EMAIL Subject', descr='Credential Issuance Failure EMAIL Subject', file_format_code='txt', model='velocity', file_txt='Échec de l''émission des informations d''identification', module_id='10006', module_name='Resident Services', template_typ_code='RS_CRE_REQ_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1243' AND lang_code='fra';
UPDATE master.template
SET "name"='موضوع EMAIL لإصدار بيانات الاعتماد', descr='موضوع EMAIL لإصدار بيانات الاعتماد', file_format_code='txt', model='velocity', file_txt='فشل إصدار بيانات الاعتماد', module_id='10006', module_name='خدمات المقيمين', template_typ_code='RS_CRE_REQ_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1243' AND lang_code='ara';
UPDATE master.template
SET "name"='ರುಜುವಾತು ವಿತರಣೆ ವಿಫಲತೆ ಇಮೇಲ್ ವಿಷಯ ', descr='ರುಜುವಾತು ವಿತರಣೆ ವಿಫಲತೆ ಇಮೇಲ್ ವಿಷಯ', file_format_code='txt', model='velocity', file_txt='ರುಜುವಾತು ವಿತರಣೆ ವಿಫಲವಾಗಿದೆ', module_id='10006', module_name='ನಿವಾಸ ಸೇವೆಗಳು', template_typ_code='RS_CRE_REQ_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1243' AND lang_code='kan';
UPDATE master.template
SET "name"='டெலிவரி டெலிவரி தோல்வி மின்னஞ்சல் பொருள்', descr='டெலிவரி டெலிவரி தோல்வி மின்னஞ்சல் பொருள்', file_format_code='txt', model='velocity', file_txt='நற்சான்றிதழ் வழங்குவதில் தோல்வி', module_id='10006', module_name='வீட்டு சேவைகள்', template_typ_code='RS_CRE_REQ_FAILURE_EMAIL_SUB', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1243' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received email subject to customize and download my card', descr='Request received email subject to customize and download my card', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1247' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received email subject to customize and download my card', descr='Request received email subject to customize and download my card', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1247' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received email subject to customize and download my card', descr='Request received email subject to customize and download my card', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1247' AND lang_code='kan';
UPDATE master.template
SET "name"='Request received email subject to customize and download my card', descr='Request received email subject to customize and download my card', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1247' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received email subject to customize and download my card', descr='Request received email subject to customize and download my card', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1247' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received email subject to customize and download my card', descr='Request received email subject to customize and download my card', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1247' AND lang_code='ara';
UPDATE master.template
SET "name"='Success email subject to customize and download my card', descr='Success email subject to customize and download my card', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1248' AND lang_code='tam';
UPDATE master.template
SET "name"='Success email subject to customize and download my card', descr='Success email subject to customize and download my card', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1248' AND lang_code='eng';
UPDATE master.template
SET "name"='Success email subject to customize and download my card', descr='Success email subject to customize and download my card', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1248' AND lang_code='ara';
UPDATE master.template
SET "name"='Success email subject to customize and download my card', descr='Success email subject to customize and download my card', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1248' AND lang_code='fra';
UPDATE master.template
SET "name"='Success email subject to customize and download my card', descr='Success email subject to customize and download my card', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1248' AND lang_code='hin';
UPDATE master.template
SET "name"='Success email subject to customize and download my card', descr='Success email subject to customize and download my card', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1248' AND lang_code='kan';
UPDATE master.template
SET "name"='Success email to customize and download my card', descr='Success email to customize and download my card', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर सफलतापूर्वक पूरा कर लिया गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
कार्ड डाउनलोड करने के लिए नीचे दिए गए लिंक पर क्लिक करें। <br>
 $downloadLink <br>
लिंक केवल 24 घंटे के लिए सक्रिय रहेगा। <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1251' AND lang_code='hin';
UPDATE master.template
SET "name"='Success email to customize and download my card', descr='Success email to customize and download my card', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails est terminée avec succès le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>
Cliquez sur le lien ci-dessous pour télécharger la carte. <br>
 $downloadLink <br>
Le lien ne sera actif que pendant 24 heures. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1251' AND lang_code='fra';
UPDATE master.template
SET "name"='Success email to customize and download my card', descr='Success email to customize and download my card', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು $date ರಂದು $time ನಲ್ಲಿ ಯಶಸ್ವಿಯಾಗಿ ಪೂರ್ಣಗೊಂಡಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಕಾರ್ಡ್ ಡೌನ್‌ಲೋಡ್ ಮಾಡಲು ಕೆಳಗಿನ ಲಿಂಕ್ ಅನ್ನು ಕ್ಲಿಕ್ ಮಾಡಿ. <br>
 $downloadLink <br>
ಲಿಂಕ್ 24 ಗಂಟೆಗಳ ಕಾಲ ಮಾತ್ರ ಸಕ್ರಿಯವಾಗಿರುತ್ತದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1251' AND lang_code='kan';
UPDATE master.template
SET "name"='Success email to customize and download my card', descr='Success email to customize and download my card', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetailsக்கான உங்கள் கோரிக்கை $date அன்று $time இல் வெற்றிகரமாக நிறைவுற்றது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
அட்டையை பதிவிறக்கம் செய்ய கீழே உள்ள இணைப்பை கிளிக் செய்யவும். <br>
 $downloadLink <br>
இணைப்பு 24 மணிநேரம் மட்டுமே செயலில் இருக்கும். <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1251' AND lang_code='tam';
UPDATE master.template
SET "name"='Success email to customize and download my card', descr='Success email to customize and download my card', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم إكمال طلبك إلى  $eventDetails بنجاح في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>
اضغط على الرابط أدناه لتنزيل البطاقة. <br>
 $downloadLink <br>
سيكون الرابط نشطًا لمدة 24 ساعة فقط. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1251' AND lang_code='ara';
UPDATE master.template
SET "name"='Success email to customize and download my card', descr='Success email to customize and download my card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails is completed successfully on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1251' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure email to customize and download my card', descr='Failure email to customize and download my card', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails émise le $date à $time a échoué. <br>
Votre identifiant d''événement est #$eventId. <br>
Connectez-vous au site Web officiel $trackServiceRequestLink pour plus de détails. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1252' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure email to customize and download my card', descr='Failure email to customize and download my card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails raised on $date at $time has failed. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1252' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure email to customize and download my card', descr='Failure email to customize and download my card', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
لقد فشل طلبك إلى  $eventDetails الذي تم رفعه في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>
قم بتسجيل الدخول إلى الموقع الرسمي  $trackServiceRequestLink لمزيد من التفاصيل. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1252' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure email to customize and download my card', descr='Failure email to customize and download my card', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$date அன்று $time இல் பெறப்பட்ட $eventDetailsக்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
மேலும் விவரங்களுக்கு அதிகாரப்பூர்வ இணையதளமான $trackServiceRequestLink இல் உள்நுழைக. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1252' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure email to customize and download my card', descr='Failure email to customize and download my card', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$date ರಂದು $time ನಲ್ಲಿ ಸಂಗ್ರಹಿಸಲಾದ $eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಹೆಚ್ಚಿನ ವಿವರಗಳಿಗಾಗಿ ಅಧಿಕೃತ ವೆಬ್‌ಸೈಟ್ $trackServiceRequestLink ಗೆ ಲಾಗ್ ಇನ್ ಮಾಡಿ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1252' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure email to customize and download my card', descr='Failure email to customize and download my card', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date पर $time पर उठाया गया विफल हो गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
अधिक जानकारी के लिए आधिकारिक वेबसाइट $trackServiceRequestLink में लॉग इन करें। <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1252' AND lang_code='hin';
UPDATE master.template
SET "name"='Success email subject to order a physical card', descr='Success email subject to order a physical card', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1254' AND lang_code='eng';
UPDATE master.template
SET "name"='Success email subject to order a physical card', descr='Success email subject to order a physical card', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1254' AND lang_code='fra';
UPDATE master.template
SET "name"='Success email subject to order a physical card', descr='Success email subject to order a physical card', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1254' AND lang_code='hin';
UPDATE master.template
SET "name"='Success email subject to order a physical card', descr='Success email subject to order a physical card', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1254' AND lang_code='kan';
UPDATE master.template
SET "name"='Success email subject to order a physical card', descr='Success email subject to order a physical card', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1254' AND lang_code='tam';
UPDATE master.template
SET "name"='Success email subject to order a physical card', descr='Success email subject to order a physical card', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1254' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure email subject to order a physical card', descr='Failure email subject to order a physical card', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1255' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure email subject to order a physical card', descr='Failure email subject to order a physical card', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1255' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure email subject to order a physical card', descr='Failure email subject to order a physical card', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1255' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure email subject to order a physical card', descr='Failure email subject to order a physical card', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1255' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure email subject to order a physical card', descr='Failure email subject to order a physical card', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1255' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure email subject to order a physical card', descr='Failure email subject to order a physical card', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1255' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received email to order a physical card', descr='Request received email to order a physical card', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails a été reçue le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1256' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received email to order a physical card', descr='Request received email to order a physical card', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetails க்கான உங்கள் கோரிக்கை $date அன்று $time இல் பெறப்பட்டது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1256' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received email to order a physical card', descr='Request received email to order a physical card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails has been received on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1256' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received email to order a physical card', descr='Request received email to order a physical card', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم استلام طلبك إلى  $eventDetails في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1256' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received email to order a physical card', descr='Request received email to order a physical card', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು $date ರಂದು $time ನಲ್ಲಿ ಸ್ವೀಕರಿಸಲಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1256' AND lang_code='kan';
UPDATE master.template
SET "name"='Request received email to order a physical card', descr='Request received email to order a physical card', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर प्राप्त हुआ है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1256' AND lang_code='hin';
UPDATE master.template
SET "name"='Success email to order a physical card', descr='Success email to order a physical card', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು $date ರಂದು $time ನಲ್ಲಿ ಯಶಸ್ವಿಯಾಗಿ ಪೂರ್ಣಗೊಂಡಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಆರ್ಡರ್ ಮಾಡಿದ ಕಾರ್ಡ್ ಅನ್ನು ಡೌನ್‌ಲೋಡ್ ಮಾಡಲು ಕೆಳಗಿನ ಲಿಂಕ್ ಅನ್ನು ಕ್ಲಿಕ್ ಮಾಡಿ. <br>
ಲಿಂಕ್ 24 ಗಂಟೆಗಳ ಕಾಲ ಮಾತ್ರ ಸಕ್ರಿಯವಾಗಿರುತ್ತದೆ. <br>
ನೀವು ಲಿಂಕ್‌ನಲ್ಲಿ $transactionID ವಹಿವಾಟು ID ಬಳಸಿಕೊಂಡು ವಹಿವಾಟಿನ ಸ್ಥಿತಿಯನ್ನು ಟ್ರ್ಯಾಕ್ ಮಾಡಬಹುದು: $trackingLink', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1257' AND lang_code='kan';
UPDATE master.template
SET "name"='Success email to order a physical card', descr='Success email to order a physical card', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails est terminée avec succès le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>
Cliquez sur le lien ci-dessous pour télécharger la carte commandée. <br>
Le lien ne sera actif que pendant 24 heures. <br>
Vous pouvez suivre l''état de la transaction à l''aide de l''ID de transaction $transactionID sur le lien : $trackingLink', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1257' AND lang_code='fra';
UPDATE master.template
SET "name"='Success email to order a physical card', descr='Success email to order a physical card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails is completed successfully on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>
You can track the status of the transaction using the transaction ID $transactionID on the link: $trackingLink', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1257' AND lang_code='eng';
UPDATE master.template
SET "name"='Success email to order a physical card', descr='Success email to order a physical card', file_format_code='txt', model='velocity', file_txt='عزيزي $name  ، <br>
تم إكمال طلبك إلى  $eventDetails بنجاح في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId . <br>
اضغط على الرابط أدناه لتنزيل البطاقة المطلوبة. <br>
سيكون الرابط نشطًا لمدة 24 ساعة فقط. <br>
يمكنك تتبع حالة المعاملة باستخدام معرف المعاملة  $transactionID على الرابط:  $trackingLink', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1257' AND lang_code='ara';
UPDATE master.template
SET "name"='Success email to order a physical card', descr='Success email to order a physical card', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर सफलतापूर्वक पूरा कर लिया गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
ऑर्डर किए गए कार्ड को डाउनलोड करने के लिए नीचे दिए गए लिंक पर क्लिक करें। <br>
लिंक केवल 24 घंटे के लिए सक्रिय रहेगा। <br>
आप लिंक पर लेन-देन आईडी $transactionID का उपयोग करके लेन-देन की स्थिति को ट्रैक कर सकते हैं: $trackingLink', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1257' AND lang_code='hin';
UPDATE master.template
SET "name"='Success email to order a physical card', descr='Success email to order a physical card', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetailsக்கான உங்கள் கோரிக்கை $date அன்று $time இல் வெற்றிகரமாக நிறைவுற்றது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
ஆர்டர் செய்யப்பட்ட அட்டையை பதிவிறக்கம் செய்ய கீழே உள்ள லிங்கை கிளிக் செய்யவும். <br>
இணைப்பு 24 மணிநேரம் மட்டுமே செயலில் இருக்கும். <br>
பரிவர்த்தனை ஐடி $transactionID ஐப் பயன்படுத்தி பரிவர்த்தனையின் நிலையைக் கண்காணிக்கலாம்: $trackingLink', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1257' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure email to order a physical card', descr='Failure email to order a physical card', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
لقد فشل طلبك إلى  $eventDetails الذي تم رفعه في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>
قم بتسجيل الدخول إلى الموقع الرسمي  $trackServiceRequestLink لمزيد من التفاصيل. <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1258' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure email to order a physical card', descr='Failure email to order a physical card', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$date ರಂದು $time ನಲ್ಲಿ ಸಂಗ್ರಹಿಸಲಾದ $eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಹೆಚ್ಚಿನ ವಿವರಗಳಿಗಾಗಿ ಅಧಿಕೃತ ವೆಬ್‌ಸೈಟ್ $trackServiceRequestLink ಗೆ ಲಾಗ್ ಇನ್ ಮಾಡಿ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1258' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure email to order a physical card', descr='Failure email to order a physical card', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$date அன்று $time இல் பெறப்பட்ட $eventDetailsக்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
மேலும் விவரங்களுக்கு அதிகாரப்பூர்வ இணையதளமான $trackServiceRequestLink இல் உள்நுழைக. <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1258' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure email to order a physical card', descr='Failure email to order a physical card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails raised on $date at $time has failed. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1258' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure email to order a physical card', descr='Failure email to order a physical card', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date पर $time पर उठाया गया विफल हो गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
अधिक जानकारी के लिए आधिकारिक वेबसाइट $trackServiceRequestLink में लॉग इन करें। <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1258' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure email to order a physical card', descr='Failure email to order a physical card', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails émise le $date à $time a échoué. <br>
Votre identifiant d''événement est #$eventId. <br>
Connectez-vous au site Web officiel $trackServiceRequestLink pour plus de détails. <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1258' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received email subject to share my credential with a partner', descr='Request received email subject to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1259' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received email subject to share my credential with a partner', descr='Request received email subject to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1259' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received email subject to share my credential with a partner', descr='Request received email subject to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1259' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received email subject to share my credential with a partner', descr='Request received email subject to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1259' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received email subject to share my credential with a partner', descr='Request received email subject to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1259' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received email subject to share my credential with a partner', descr='Request received email subject to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1259' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure email subject to share my credential with a partner', descr='Failure email subject to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1261' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure email subject to share my credential with a partner', descr='Failure email subject to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1261' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure email subject to share my credential with a partner', descr='Failure email subject to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1261' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure email subject to share my credential with a partner', descr='Failure email subject to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1261' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure email subject to share my credential with a partner', descr='Failure email subject to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1261' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure email subject to share my credential with a partner', descr='Failure email subject to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1261' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received email to share my credential with a partner', descr='Request received email to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर प्राप्त हुआ है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1262' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received email to share my credential with a partner', descr='Request received email to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails has been received on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1262' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received email to share my credential with a partner', descr='Request received email to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم استلام طلبك إلى  $eventDetails في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1262' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received email to share my credential with a partner', descr='Request received email to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetails க்கான உங்கள் கோரிக்கை $date அன்று $time இல் பெறப்பட்டது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1262' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received email to share my credential with a partner', descr='Request received email to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails a été reçue le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1262' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received email to share my credential with a partner', descr='Request received email to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು $date ರಂದು $time ನಲ್ಲಿ ಸ್ವೀಕರಿಸಲಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1262' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure email to lock/unlock authentication', descr='Failure email to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails raised on $date at $time has failed. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1270' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure email to lock/unlock authentication', descr='Failure email to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
لقد فشل طلبك إلى  $eventDetails الذي تم رفعه في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>
قم بتسجيل الدخول إلى الموقع الرسمي  $trackServiceRequestLink لمزيد من التفاصيل. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1270' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure email to lock/unlock authentication', descr='Failure email to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails émise le $date à $time a échoué. <br>
Votre identifiant d''événement est #$eventId. <br>
Connectez-vous au site Web officiel $trackServiceRequestLink pour plus de détails. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1270' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure email to lock/unlock authentication', descr='Failure email to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date पर $time पर उठाया गया विफल हो गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
अधिक जानकारी के लिए आधिकारिक वेबसाइट $trackServiceRequestLink में लॉग इन करें। <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1270' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure email to lock/unlock authentication', descr='Failure email to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$date ರಂದು $time ನಲ್ಲಿ ಸಂಗ್ರಹಿಸಲಾದ $eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಹೆಚ್ಚಿನ ವಿವರಗಳಿಗಾಗಿ ಅಧಿಕೃತ ವೆಬ್‌ಸೈಟ್ $trackServiceRequestLink ಗೆ ಲಾಗ್ ಇನ್ ಮಾಡಿ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1270' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure email to lock/unlock authentication', descr='Failure email to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$date அன்று $time இல் பெறப்பட்ட $eventDetailsக்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
மேலும் விவரங்களுக்கு அதிகாரப்பூர்வ இணையதளமான $trackServiceRequestLink இல் உள்நுழைக. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1270' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received email subject to self update demographic data', descr='Request received email subject to self update demographic data', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1271' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received email subject to self update demographic data', descr='Request received email subject to self update demographic data', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1271' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received email subject to self update demographic data', descr='Request received email subject to self update demographic data', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1271' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received email subject to self update demographic data', descr='Request received email subject to self update demographic data', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1271' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received email subject to self update demographic data', descr='Request received email subject to self update demographic data', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1271' AND lang_code='kan';
UPDATE master.template
SET "name"='Request received email subject to self update demographic data', descr='Request received email subject to self update demographic data', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1271' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure email subject to get my UIN card', descr='Failure email subject to get my UIN card', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1285' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure email subject to get my UIN card', descr='Failure email subject to get my UIN card', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1285' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure email subject to get my UIN card', descr='Failure email subject to get my UIN card', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1285' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure email subject to get my UIN card', descr='Failure email subject to get my UIN card', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1285' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure email subject to get my UIN card', descr='Failure email subject to get my UIN card', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1285' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure email subject to get my UIN card', descr='Failure email subject to get my UIN card', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1285' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received email to get my UIN card', descr='Request received email to get my UIN card', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails a été reçue le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1286' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received email to get my UIN card', descr='Request received email to get my UIN card', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم استلام طلبك إلى  $eventDetails في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1286' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received email to get my UIN card', descr='Request received email to get my UIN card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails has been received on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1286' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received email to get my UIN card', descr='Request received email to get my UIN card', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು $date ರಂದು $time ನಲ್ಲಿ ಸ್ವೀಕರಿಸಲಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1286' AND lang_code='kan';
UPDATE master.template
SET "name"='Request received email to get my UIN card', descr='Request received email to get my UIN card', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर प्राप्त हुआ है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1286' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received email to get my UIN card', descr='Request received email to get my UIN card', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetails க்கான உங்கள் கோரிக்கை $date அன்று $time இல் பெறப்பட்டது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1286' AND lang_code='tam';
UPDATE master.template
SET "name"='Success email to get my UIN card', descr='Success email to get my UIN card', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर सफलतापूर्वक पूरा कर लिया गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1287' AND lang_code='hin';
UPDATE master.template
SET "name"='Success email to get my UIN card', descr='Success email to get my UIN card', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم إكمال طلبك إلى  $eventDetails بنجاح في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1287' AND lang_code='ara';
UPDATE master.template
SET "name"='Success email to get my UIN card', descr='Success email to get my UIN card', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು $date ರಂದು $time ನಲ್ಲಿ ಯಶಸ್ವಿಯಾಗಿ ಪೂರ್ಣಗೊಂಡಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1287' AND lang_code='kan';
UPDATE master.template
SET "name"='Success email to get my UIN card', descr='Success email to get my UIN card', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetailsக்கான உங்கள் கோரிக்கை $date அன்று $time இல் வெற்றிகரமாக நிறைவுற்றது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1287' AND lang_code='tam';
UPDATE master.template
SET "name"='Success email to get my UIN card', descr='Success email to get my UIN card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails is completed successfully on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1287' AND lang_code='eng';
UPDATE master.template
SET "name"='Success email to get my UIN card', descr='Success email to get my UIN card', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails est terminée avec succès le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1287' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure email to get my UIN card', descr='Failure email to get my UIN card', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
لقد فشل طلبك إلى  $eventDetails الذي تم رفعه في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>
قم بتسجيل الدخول إلى الموقع الرسمي  $trackServiceRequestLink لمزيد من التفاصيل. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1288' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure email to get my UIN card', descr='Failure email to get my UIN card', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails émise le $date à $time a échoué. <br>
Votre identifiant d''événement est #$eventId. <br>
Connectez-vous au site Web officiel $trackServiceRequestLink pour plus de détails. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1288' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure email to get my UIN card', descr='Failure email to get my UIN card', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date पर $time पर उठाया गया विफल हो गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
अधिक जानकारी के लिए आधिकारिक वेबसाइट $trackServiceRequestLink में लॉग इन करें। <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1288' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure email to get my UIN card', descr='Failure email to get my UIN card', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$date ರಂದು $time ನಲ್ಲಿ ಸಂಗ್ರಹಿಸಲಾದ $eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಹೆಚ್ಚಿನ ವಿವರಗಳಿಗಾಗಿ ಅಧಿಕೃತ ವೆಬ್‌ಸೈಟ್ $trackServiceRequestLink ಗೆ ಲಾಗ್ ಇನ್ ಮಾಡಿ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1288' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure email to get my UIN card', descr='Failure email to get my UIN card', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$date அன்று $time இல் பெறப்பட்ட $eventDetailsக்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
மேலும் விவரங்களுக்கு அதிகாரப்பூர்வ இணையதளமான $trackServiceRequestLink இல் உள்நுழைக. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1288' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure email to get my UIN card', descr='Failure email to get my UIN card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails raised on $date at $time has failed. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1288' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received email subject to verify my phone and email', descr='Request received email subject to verify my phone and email', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1289' AND lang_code='kan';
UPDATE master.template
SET "name"='Request received email subject to verify my phone and email', descr='Request received email subject to verify my phone and email', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1289' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received email subject to verify my phone and email', descr='Request received email subject to verify my phone and email', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1289' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received email subject to verify my phone and email', descr='Request received email subject to verify my phone and email', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1289' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received email subject to verify my phone and email', descr='Request received email subject to verify my phone and email', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1289' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received email subject to verify my phone and email', descr='Request received email subject to verify my phone and email', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1289' AND lang_code='tam';
UPDATE master.template
SET "name"='Success email subject to verify my phone and email', descr='Success email subject to verify my phone and email', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1290' AND lang_code='hin';
UPDATE master.template
SET "name"='Success email subject to verify my phone and email', descr='Success email subject to verify my phone and email', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1290' AND lang_code='eng';
UPDATE master.template
SET "name"='Success email subject to verify my phone and email', descr='Success email subject to verify my phone and email', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1290' AND lang_code='ara';
UPDATE master.template
SET "name"='Success email subject to verify my phone and email', descr='Success email subject to verify my phone and email', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1290' AND lang_code='fra';
UPDATE master.template
SET "name"='Success email subject to verify my phone and email', descr='Success email subject to verify my phone and email', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1290' AND lang_code='kan';
UPDATE master.template
SET "name"='Success email subject to verify my phone and email', descr='Success email subject to verify my phone and email', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1290' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure email subject to verify my phone and email', descr='Failure email subject to verify my phone and email', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1291' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure email subject to verify my phone and email', descr='Failure email subject to verify my phone and email', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1291' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure email subject to verify my phone and email', descr='Failure email subject to verify my phone and email', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1291' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure email subject to verify my phone and email', descr='Failure email subject to verify my phone and email', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1291' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure email subject to verify my phone and email', descr='Failure email subject to verify my phone and email', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1291' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure email subject to verify my phone and email', descr='Failure email subject to verify my phone and email', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1291' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received email to verify my phone and email', descr='Request received email to verify my phone and email', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು $date ರಂದು $time ನಲ್ಲಿ ಸ್ವೀಕರಿಸಲಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1292' AND lang_code='kan';
UPDATE master.template
SET "name"='Request received email to verify my phone and email', descr='Request received email to verify my phone and email', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم استلام طلبك إلى  $eventDetails في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1292' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received email to verify my phone and email', descr='Request received email to verify my phone and email', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails a été reçue le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1292' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received email to verify my phone and email', descr='Request received email to verify my phone and email', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर प्राप्त हुआ है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1292' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received email to verify my phone and email', descr='Request received email to verify my phone and email', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails has been received on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1292' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received email to verify my phone and email', descr='Request received email to verify my phone and email', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetails க்கான உங்கள் கோரிக்கை $date அன்று $time இல் பெறப்பட்டது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1292' AND lang_code='tam';
UPDATE master.template
SET "name"='Success email to verify my phone and email', descr='Success email to verify my phone and email', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर सफलतापूर्वक पूरा कर लिया गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1293' AND lang_code='hin';
UPDATE master.template
SET "name"='Success email to verify my phone and email', descr='Success email to verify my phone and email', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails is completed successfully on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1293' AND lang_code='eng';
UPDATE master.template
SET "name"='Success email to verify my phone and email', descr='Success email to verify my phone and email', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails est terminée avec succès le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1293' AND lang_code='fra';
UPDATE master.template
SET "name"='Success email to verify my phone and email', descr='Success email to verify my phone and email', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetailsக்கான உங்கள் கோரிக்கை $date அன்று $time இல் வெற்றிகரமாக நிறைவுற்றது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1293' AND lang_code='tam';
UPDATE master.template
SET "name"='Success email to verify my phone and email', descr='Success email to verify my phone and email', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم إكمال طلبك إلى  $eventDetails بنجاح في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1293' AND lang_code='ara';
UPDATE master.template
SET "name"='Success email to verify my phone and email', descr='Success email to verify my phone and email', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು $date ರಂದು $time ನಲ್ಲಿ ಯಶಸ್ವಿಯಾಗಿ ಪೂರ್ಣಗೊಂಡಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1293' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure email to verify my phone and email', descr='Failure email to verify my phone and email', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails raised on $date at $time has failed. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1294' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure email to verify my phone and email', descr='Failure email to verify my phone and email', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$date ರಂದು $time ನಲ್ಲಿ ಸಂಗ್ರಹಿಸಲಾದ $eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಹೆಚ್ಚಿನ ವಿವರಗಳಿಗಾಗಿ ಅಧಿಕೃತ ವೆಬ್‌ಸೈಟ್ $trackServiceRequestLink ಗೆ ಲಾಗ್ ಇನ್ ಮಾಡಿ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1294' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure email to verify my phone and email', descr='Failure email to verify my phone and email', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$date அன்று $time இல் பெறப்பட்ட $eventDetailsக்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
மேலும் விவரங்களுக்கு அதிகாரப்பூர்வ இணையதளமான $trackServiceRequestLink இல் உள்நுழைக. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1294' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure email to verify my phone and email', descr='Failure email to verify my phone and email', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails émise le $date à $time a échoué. <br>
Votre identifiant d''événement est #$eventId. <br>
Connectez-vous au site Web officiel $trackServiceRequestLink pour plus de détails. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1294' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure email to verify my phone and email', descr='Failure email to verify my phone and email', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
لقد فشل طلبك إلى  $eventDetails الذي تم رفعه في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>
قم بتسجيل الدخول إلى الموقع الرسمي  $trackServiceRequestLink لمزيد من التفاصيل. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1294' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure email to verify my phone and email', descr='Failure email to verify my phone and email', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date पर $time पर उठाया गया विफल हो गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
अधिक जानकारी के लिए आधिकारिक वेबसाइट $trackServiceRequestLink में लॉग इन करें। <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1294' AND lang_code='hin';
UPDATE master.template
SET "name"='Acknowledgement for share credential with a partner', descr='Acknowledgement for share credential with a partner', file_format_code='txt', model='velocity', file_txt='<html>

<head>
    
</head>

<body lang=EN-US link=blue vlink="#954F72" style=''tab-interval:.5in;word-wrap:break-word''>

<div class=WordSection1>

<div>

<p class=MsoNormal align=center style=''text-align:center''><span lang=EN-IN><img
width=200 height=100 id="Picture 2"
src="https://mosip.io/images/logo.png" alt="MOSIP"></span></p>
<p class=MsoNormal align=center style=''text-align:center''><span lang=EN-IN><img
width=50 height=50 id="partnerlogo"
src="$partnerLogo" alt="partner logo"></span></p>

<table class=MsoNormalTable align=center border=0 cellspacing=0 cellpadding=0 width=586
 style=''width:439.4pt;border-collapse:collapse;mso-yfti-tbllook:
 1184;mso-padding-alt:0in 0in 0in 0in''>
 <tr style=''mso-yfti-irow:0;mso-yfti-firstrow:yes''>
  <td width=586 colspan=2 valign=top style=''width:439.4pt;border:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph align=center style=''margin:0in;text-align:center''><b><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Request details</span></b></p>
  </td>
 </tr>
 
 <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event request timestamp: </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$timestamp</span></p></td>
  </tr>
  
  <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event Id:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventId</span></p></td>
  </tr>
  
  <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Authentication mode:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$authenticationMode</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event Type:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventType</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Purpose:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$purpose</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event status:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventStatus</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Summary  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$summary</span></p></td>
  </tr>
 <tr style=''mso-yfti-irow:2''>
  <td width=586 colspan=2 valign=top style=''width:439.4pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph align=center style=''margin:0in;text-align:center''><b><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Data share details</span></b></p>
  </td>
 </tr>
 <tr style=''mso-yfti-irow:3''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>Attributes shared with the partner:</span></p>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>&nbsp;</span></p>
  <p class=MsoListParagraph style=''margin:0in''><b><span lang=EN-IN
  style=''mso-ansi-language:EN-IN''>&nbsp;</span></b></p>
  </td>
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$attributeList</span></span></p>
  <p class=MsoListParagraph style=''margin:0in''><b><span lang=EN-IN
  style=''mso-ansi-language:EN-IN''>&nbsp;</span></b></p>
  </td>
 </tr>
 <tr style=''mso-yfti-irow:4''>
  <td width=586 colspan=2 valign=top style=''width:439.4pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph align=center style=''margin:0in;text-align:center''><b><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Partner Details</span></b></p>
  </td>
 </tr>
 <tr style=''mso-yfti-irow:5;mso-yfti-lastrow:yes''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>Partner name:</span></p>
  </td>
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$partnerName</span><o:p></o:p></span></p>
  </td>
 </tr>
</table>

<p class=MsoListParagraph align=center style=''text-align:center''><span
lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
mso-ansi-language:EN-IN''>&nbsp;</span></p>

<p class=MsoListParagraph align=center style=''text-align:center''><span
lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
mso-ansi-language:EN-IN''>To view the status of your event, return to&nbsp;</span><span
lang=EN-IN style=''mso-ansi-language:EN-IN''><a
href=""><span
style=''font-family:"Verdana",sans-serif;color:#004B91;background:white''><a href="$trackServiceRequestLink">Event
Summary</a></span></a></span><span lang=EN-IN style=''font-family:"Verdana",sans-serif;
color:black;background:white;mso-ansi-language:EN-IN''>.</span></p>

<p class=MsoNormal style=''mso-margin-top-alt:auto;mso-margin-bottom-alt:auto''><span
lang=EN-IN style=''mso-ansi-language:EN-IN''>&nbsp;</span></p>

</div>

</div>

</body>

</html>', module_id='10006', module_name='Resident Services', template_typ_code='acknowledgement-share-cred-with-partner', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1295' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received sms to customize and download my card', descr='Request received sms to customize and download my card', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails a été reçue le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1296' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received sms to customize and download my card', descr='Request received sms to customize and download my card', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetails க்கான உங்கள் கோரிக்கை $date அன்று $time இல் பெறப்பட்டது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1296' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received sms to customize and download my card', descr='Request received sms to customize and download my card', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು $date ರಂದು $time ನಲ್ಲಿ ಸ್ವೀಕರಿಸಲಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1296' AND lang_code='kan';
UPDATE master.template
SET "name"='Request received sms to customize and download my card', descr='Request received sms to customize and download my card', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर प्राप्त हुआ है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1296' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received sms to customize and download my card', descr='Request received sms to customize and download my card', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم استلام طلبك إلى  $eventDetails في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1296' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received sms to customize and download my card', descr='Request received sms to customize and download my card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails has been received on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1296' AND lang_code='eng';
UPDATE master.template
SET "name"='Success sms to customize and download my card', descr='Success sms to customize and download my card', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು $date ರಂದು $time ನಲ್ಲಿ ಯಶಸ್ವಿಯಾಗಿ ಪೂರ್ಣಗೊಂಡಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಕಾರ್ಡ್ ಡೌನ್‌ಲೋಡ್ ಮಾಡಲು ಕೆಳಗಿನ ಲಿಂಕ್ ಅನ್ನು ಕ್ಲಿಕ್ ಮಾಡಿ. <br>
 $downloadLink <br>
ಲಿಂಕ್ 24 ಗಂಟೆಗಳ ಕಾಲ ಮಾತ್ರ ಸಕ್ರಿಯವಾಗಿರುತ್ತದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1297' AND lang_code='kan';
UPDATE master.template
SET "name"='Success sms to customize and download my card', descr='Success sms to customize and download my card', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم إكمال طلبك إلى  $eventDetails بنجاح في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>
اضغط على الرابط أدناه لتنزيل البطاقة. <br>
 $downloadLink <br>
سيكون الرابط نشطًا لمدة 24 ساعة فقط. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1297' AND lang_code='ara';
UPDATE master.template
SET "name"='Success sms to customize and download my card', descr='Success sms to customize and download my card', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर सफलतापूर्वक पूरा कर लिया गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
कार्ड डाउनलोड करने के लिए नीचे दिए गए लिंक पर क्लिक करें। <br>
 $downloadLink <br>
लिंक केवल 24 घंटे के लिए सक्रिय रहेगा। <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1297' AND lang_code='hin';
UPDATE master.template
SET "name"='Success sms to customize and download my card', descr='Success sms to customize and download my card', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetailsக்கான உங்கள் கோரிக்கை $date அன்று $time இல் வெற்றிகரமாக நிறைவுற்றது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
அட்டையை பதிவிறக்கம் செய்ய கீழே உள்ள இணைப்பை கிளிக் செய்யவும். <br>
 $downloadLink <br>
இணைப்பு 24 மணிநேரம் மட்டுமே செயலில் இருக்கும். <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1297' AND lang_code='tam';
UPDATE master.template
SET "name"='Success sms to customize and download my card', descr='Success sms to customize and download my card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails is completed successfully on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1297' AND lang_code='eng';
UPDATE master.template
SET "name"='Success sms to customize and download my card', descr='Success sms to customize and download my card', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails est terminée avec succès le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>
Cliquez sur le lien ci-dessous pour télécharger la carte. <br>
 $downloadLink <br>
Le lien ne sera actif que pendant 24 heures. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1297' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure sms to customize and download my card', descr='Failure sms to customize and download my card', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$date அன்று $time இல் பெறப்பட்ட $eventDetailsக்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
மேலும் விவரங்களுக்கு அதிகாரப்பூர்வ இணையதளமான $trackServiceRequestLink இல் உள்நுழைக. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1298' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure sms to customize and download my card', descr='Failure sms to customize and download my card', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date पर $time पर उठाया गया विफल हो गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
अधिक जानकारी के लिए आधिकारिक वेबसाइट $trackServiceRequestLink में लॉग इन करें। <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1298' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure sms to customize and download my card', descr='Failure sms to customize and download my card', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$date ರಂದು $time ನಲ್ಲಿ ಸಂಗ್ರಹಿಸಲಾದ $eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಹೆಚ್ಚಿನ ವಿವರಗಳಿಗಾಗಿ ಅಧಿಕೃತ ವೆಬ್‌ಸೈಟ್ $trackServiceRequestLink ಗೆ ಲಾಗ್ ಇನ್ ಮಾಡಿ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1298' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure sms to customize and download my card', descr='Failure sms to customize and download my card', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
لقد فشل طلبك إلى  $eventDetails الذي تم رفعه في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>
قم بتسجيل الدخول إلى الموقع الرسمي  $trackServiceRequestLink لمزيد من التفاصيل. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1298' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure sms to customize and download my card', descr='Failure sms to customize and download my card', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails émise le $date à $time a échoué. <br>
Votre identifiant d''événement est #$eventId. <br>
Connectez-vous au site Web officiel $trackServiceRequestLink pour plus de détails. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1298' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure sms to customize and download my card', descr='Failure sms to customize and download my card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails raised on $date at $time has failed. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1298' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received sms to order a physical card', descr='Request received sms to order a physical card', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم استلام طلبك إلى  $eventDetails في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1299' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received sms to order a physical card', descr='Request received sms to order a physical card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails has been received on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1299' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received sms to order a physical card', descr='Request received sms to order a physical card', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails a été reçue le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1299' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received sms to order a physical card', descr='Request received sms to order a physical card', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर प्राप्त हुआ है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1299' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received sms to order a physical card', descr='Request received sms to order a physical card', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು $date ರಂದು $time ನಲ್ಲಿ ಸ್ವೀಕರಿಸಲಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1299' AND lang_code='kan';
UPDATE master.template
SET "name"='Request received sms to order a physical card', descr='Request received sms to order a physical card', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetails க்கான உங்கள் கோரிக்கை $date அன்று $time இல் பெறப்பட்டது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1299' AND lang_code='tam';
UPDATE master.template
SET "name"='Success sms to order a physical card', descr='Success sms to order a physical card', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetailsக்கான உங்கள் கோரிக்கை $date அன்று $time இல் வெற்றிகரமாக நிறைவுற்றது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
ஆர்டர் செய்யப்பட்ட அட்டையை பதிவிறக்கம் செய்ய கீழே உள்ள லிங்கை கிளிக் செய்யவும். <br>
இணைப்பு 24 மணிநேரம் மட்டுமே செயலில் இருக்கும். <br>
பரிவர்த்தனை ஐடி $transactionID ஐப் பயன்படுத்தி பரிவர்த்தனையின் நிலையைக் கண்காணிக்கலாம்: $trackingLink', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1300' AND lang_code='tam';
UPDATE master.template
SET "name"='Success sms to order a physical card', descr='Success sms to order a physical card', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು $date ರಂದು $time ನಲ್ಲಿ ಯಶಸ್ವಿಯಾಗಿ ಪೂರ್ಣಗೊಂಡಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಆರ್ಡರ್ ಮಾಡಿದ ಕಾರ್ಡ್ ಅನ್ನು ಡೌನ್‌ಲೋಡ್ ಮಾಡಲು ಕೆಳಗಿನ ಲಿಂಕ್ ಅನ್ನು ಕ್ಲಿಕ್ ಮಾಡಿ. <br>
ಲಿಂಕ್ 24 ಗಂಟೆಗಳ ಕಾಲ ಮಾತ್ರ ಸಕ್ರಿಯವಾಗಿರುತ್ತದೆ. <br>
ನೀವು ಲಿಂಕ್‌ನಲ್ಲಿ $transactionID ವಹಿವಾಟು ID ಬಳಸಿಕೊಂಡು ವಹಿವಾಟಿನ ಸ್ಥಿತಿಯನ್ನು ಟ್ರ್ಯಾಕ್ ಮಾಡಬಹುದು: $trackingLink', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1300' AND lang_code='kan';
UPDATE master.template
SET "name"='Success sms to order a physical card', descr='Success sms to order a physical card', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर सफलतापूर्वक पूरा कर लिया गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
ऑर्डर किए गए कार्ड को डाउनलोड करने के लिए नीचे दिए गए लिंक पर क्लिक करें। <br>
लिंक केवल 24 घंटे के लिए सक्रिय रहेगा। <br>
आप लिंक पर लेन-देन आईडी $transactionID का उपयोग करके लेन-देन की स्थिति को ट्रैक कर सकते हैं: $trackingLink', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1300' AND lang_code='hin';
UPDATE master.template
SET "name"='Success sms to order a physical card', descr='Success sms to order a physical card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails is completed successfully on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>
You can track the status of the transaction using the transaction ID $transactionID on the link: $trackingLink', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1300' AND lang_code='eng';
UPDATE master.template
SET "name"='Success sms to order a physical card', descr='Success sms to order a physical card', file_format_code='txt', model='velocity', file_txt='عزيزي $name  ، <br>
تم إكمال طلبك إلى  $eventDetails بنجاح في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId . <br>
اضغط على الرابط أدناه لتنزيل البطاقة المطلوبة. <br>
سيكون الرابط نشطًا لمدة 24 ساعة فقط. <br>
يمكنك تتبع حالة المعاملة باستخدام معرف المعاملة  $transactionID على الرابط:  $trackingLink', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1300' AND lang_code='ara';
UPDATE master.template
SET "name"='Success sms to order a physical card', descr='Success sms to order a physical card', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails est terminée avec succès le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>
Cliquez sur le lien ci-dessous pour télécharger la carte commandée. <br>
Le lien ne sera actif que pendant 24 heures. <br>
Vous pouvez suivre l''état de la transaction à l''aide de l''ID de transaction $transactionID sur le lien : $trackingLink', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1300' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure sms to order a physical card', descr='Failure sms to order a physical card', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
لقد فشل طلبك إلى  $eventDetails الذي تم رفعه في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>
قم بتسجيل الدخول إلى الموقع الرسمي  $trackServiceRequestLink لمزيد من التفاصيل. <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1301' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure sms to order a physical card', descr='Failure sms to order a physical card', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails émise le $date à $time a échoué. <br>
Votre identifiant d''événement est #$eventId. <br>
Connectez-vous au site Web officiel $trackServiceRequestLink pour plus de détails. <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1301' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure sms to order a physical card', descr='Failure sms to order a physical card', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$date அன்று $time இல் பெறப்பட்ட $eventDetailsக்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
மேலும் விவரங்களுக்கு அதிகாரப்பூர்வ இணையதளமான $trackServiceRequestLink இல் உள்நுழைக. <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1301' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure sms to order a physical card', descr='Failure sms to order a physical card', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date पर $time पर उठाया गया विफल हो गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
अधिक जानकारी के लिए आधिकारिक वेबसाइट $trackServiceRequestLink में लॉग इन करें। <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1301' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure sms to order a physical card', descr='Failure sms to order a physical card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails raised on $date at $time has failed. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1301' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure sms to order a physical card', descr='Failure sms to order a physical card', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$date ರಂದು $time ನಲ್ಲಿ ಸಂಗ್ರಹಿಸಲಾದ $eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಹೆಚ್ಚಿನ ವಿವರಗಳಿಗಾಗಿ ಅಧಿಕೃತ ವೆಬ್‌ಸೈಟ್ $trackServiceRequestLink ಗೆ ಲಾಗ್ ಇನ್ ಮಾಡಿ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1301' AND lang_code='kan';
UPDATE master.template
SET "name"='Request received sms to share my credential with a partner', descr='Request received sms to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails a été reçue le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1302' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received sms to share my credential with a partner', descr='Request received sms to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails has been received on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1302' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received sms to share my credential with a partner', descr='Request received sms to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم استلام طلبك إلى  $eventDetails في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1302' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received sms to share my credential with a partner', descr='Request received sms to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर प्राप्त हुआ है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1302' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received sms to share my credential with a partner', descr='Request received sms to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು $date ರಂದು $time ನಲ್ಲಿ ಸ್ವೀಕರಿಸಲಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1302' AND lang_code='kan';
UPDATE master.template
SET "name"='Request received sms to share my credential with a partner', descr='Request received sms to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetails க்கான உங்கள் கோரிக்கை $date அன்று $time இல் பெறப்பட்டது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1302' AND lang_code='tam';
UPDATE master.template
SET "name"='Success sms to share my credential with a partner', descr='Success sms to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails avec $partner est terminée avec succès le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1303' AND lang_code='fra';
UPDATE master.template
SET "name"='Success sms to share my credential with a partner', descr='Success sms to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$Partner உடனான $eventDetailsக்கான உங்கள் கோரிக்கை $date அன்று $time இல் வெற்றிகரமாக நிறைவடைந்தது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1303' AND lang_code='tam';
UPDATE master.template
SET "name"='Success sms to share my credential with a partner', descr='Success sms to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$Partner ಜೊತೆಗೆ $eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು $date ರಂದು $time ನಲ್ಲಿ ಯಶಸ್ವಿಯಾಗಿ ಪೂರ್ಣಗೊಳಿಸಲಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1303' AND lang_code='kan';
UPDATE master.template
SET "name"='Success sms to share my credential with a partner', descr='Success sms to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails ($partner) is completed successfully on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1303' AND lang_code='eng';
UPDATE master.template
SET "name"='Success sms to share my credential with a partner', descr='Success sms to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$ पार्टनर के साथ $eventDetails के लिए आपका अनुरोध $date पर $time पर सफलतापूर्वक पूरा हो गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1303' AND lang_code='hin';
UPDATE master.template
SET "name"='Success sms to share my credential with a partner', descr='Success sms to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم إكمال طلبك إلى  $eventDetails مع  $partner بنجاح في  $date عند  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>
يمكنك تتبع حالة المعاملة باستخدام معرف المعاملة  $transactionID على الرابط  $trackingLink', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1303' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure sms to share my credential with a partner', descr='Failure sms to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
لقد فشل طلبك إلى  $eventDetails الذي تم رفعه في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>
قم بتسجيل الدخول إلى الموقع الرسمي  $trackServiceRequestLink لمزيد من التفاصيل. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1304' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure sms to share my credential with a partner', descr='Failure sms to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails émise le $date à $time a échoué. <br>
Votre identifiant d''événement est #$eventId. <br>
Connectez-vous au site Web officiel $trackServiceRequestLink pour plus de détails. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1304' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure sms to share my credential with a partner', descr='Failure sms to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date पर $time पर उठाया गया विफल हो गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
अधिक जानकारी के लिए आधिकारिक वेबसाइट $trackServiceRequestLink में लॉग इन करें। <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1304' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure sms to share my credential with a partner', descr='Failure sms to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$date ರಂದು $time ನಲ್ಲಿ ಸಂಗ್ರಹಿಸಲಾದ $eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಹೆಚ್ಚಿನ ವಿವರಗಳಿಗಾಗಿ ಅಧಿಕೃತ ವೆಬ್‌ಸೈಟ್ $trackServiceRequestLink ಗೆ ಲಾಗ್ ಇನ್ ಮಾಡಿ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1304' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure sms to share my credential with a partner', descr='Failure sms to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$date அன்று $time இல் பெறப்பட்ட $eventDetailsக்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
மேலும் விவரங்களுக்கு அதிகாரப்பூர்வ இணையதளமான $trackServiceRequestLink இல் உள்நுழைக. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1304' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure sms to share my credential with a partner', descr='Failure sms to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails raised on $date at $time has failed. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1304' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received sms to lock/unlock authentication', descr='Request received sms to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು $date ರಂದು $time ನಲ್ಲಿ ಸ್ವೀಕರಿಸಲಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1305' AND lang_code='kan';
UPDATE master.template
SET "name"='Request received sms to lock/unlock authentication', descr='Request received sms to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم استلام طلبك إلى  $eventDetails في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1305' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received sms to lock/unlock authentication', descr='Request received sms to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetails க்கான உங்கள் கோரிக்கை $date அன்று $time இல் பெறப்பட்டது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1305' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received sms to lock/unlock authentication', descr='Request received sms to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails a été reçue le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1305' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received sms to lock/unlock authentication', descr='Request received sms to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर प्राप्त हुआ है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1305' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received sms to lock/unlock authentication', descr='Request received sms to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails has been received on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1305' AND lang_code='eng';
UPDATE master.template
SET "name"='Success sms to lock/unlock authentication', descr='Success sms to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails is completed successfully on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1306' AND lang_code='eng';
UPDATE master.template
SET "name"='Success sms to lock/unlock authentication', descr='Success sms to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು $date ರಂದು $time ನಲ್ಲಿ ಯಶಸ್ವಿಯಾಗಿ ಪೂರ್ಣಗೊಂಡಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1306' AND lang_code='kan';
UPDATE master.template
SET "name"='Success sms to lock/unlock authentication', descr='Success sms to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर सफलतापूर्वक पूरा कर लिया गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1306' AND lang_code='hin';
UPDATE master.template
SET "name"='Success sms to lock/unlock authentication', descr='Success sms to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails est terminée avec succès le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1306' AND lang_code='fra';
UPDATE master.template
SET "name"='Success sms to lock/unlock authentication', descr='Success sms to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetailsக்கான உங்கள் கோரிக்கை $date அன்று $time இல் வெற்றிகரமாக நிறைவுற்றது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1306' AND lang_code='tam';
UPDATE master.template
SET "name"='Success sms to lock/unlock authentication', descr='Success sms to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم إكمال طلبك إلى  $eventDetails بنجاح في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1306' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure sms to lock/unlock authentication', descr='Failure sms to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails émise le $date à $time a échoué. <br>
Votre identifiant d''événement est #$eventId. <br>
Connectez-vous au site Web officiel $trackServiceRequestLink pour plus de détails. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1307' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure sms to lock/unlock authentication', descr='Failure sms to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails raised on $date at $time has failed. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1307' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure sms to lock/unlock authentication', descr='Failure sms to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
لقد فشل طلبك إلى  $eventDetails الذي تم رفعه في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>
قم بتسجيل الدخول إلى الموقع الرسمي  $trackServiceRequestLink لمزيد من التفاصيل. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1307' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure sms to lock/unlock authentication', descr='Failure sms to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date पर $time पर उठाया गया विफल हो गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
अधिक जानकारी के लिए आधिकारिक वेबसाइट $trackServiceRequestLink में लॉग इन करें। <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1307' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure sms to lock/unlock authentication', descr='Failure sms to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$date ರಂದು $time ನಲ್ಲಿ ಸಂಗ್ರಹಿಸಲಾದ $eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಹೆಚ್ಚಿನ ವಿವರಗಳಿಗಾಗಿ ಅಧಿಕೃತ ವೆಬ್‌ಸೈಟ್ $trackServiceRequestLink ಗೆ ಲಾಗ್ ಇನ್ ಮಾಡಿ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1307' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure sms to lock/unlock authentication', descr='Failure sms to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$date அன்று $time இல் பெறப்பட்ட $eventDetailsக்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
மேலும் விவரங்களுக்கு அதிகாரப்பூர்வ இணையதளமான $trackServiceRequestLink இல் உள்நுழைக. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1307' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received sms to self update demographic data', descr='Request received sms to self update demographic data', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails has been received on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1308' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received sms to self update demographic data', descr='Request received sms to self update demographic data', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم استلام طلبك إلى  $eventDetails في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1308' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received sms to self update demographic data', descr='Request received sms to self update demographic data', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर प्राप्त हुआ है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1308' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received sms to self update demographic data', descr='Request received sms to self update demographic data', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails a été reçue le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1308' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received sms to self update demographic data', descr='Request received sms to self update demographic data', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು $date ರಂದು $time ನಲ್ಲಿ ಸ್ವೀಕರಿಸಲಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1308' AND lang_code='kan';
UPDATE master.template
SET "name"='Request received sms to self update demographic data', descr='Request received sms to self update demographic data', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetails க்கான உங்கள் கோரிக்கை $date அன்று $time இல் பெறப்பட்டது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1308' AND lang_code='tam';
UPDATE master.template
SET "name"='Success sms to self update demographic data', descr='Success sms to self update demographic data', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetailsக்கான உங்கள் கோரிக்கை $date அன்று $time இல் வெற்றிகரமாக நிறைவுற்றது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
புதிய டேட்டாவுடன் உங்கள் கார்டைப் பதிவிறக்க கீழே உள்ள இணைப்பைக் கிளிக் செய்யவும். <br>
இணைப்பு 24 மணிநேரம் மட்டுமே செயலில் இருக்கும். <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1309' AND lang_code='tam';
UPDATE master.template
SET "name"='Success sms to self update demographic data', descr='Success sms to self update demographic data', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails is completed successfully on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1309' AND lang_code='eng';
UPDATE master.template
SET "name"='Success sms to self update demographic data', descr='Success sms to self update demographic data', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم إكمال طلبك إلى  $eventDetails بنجاح في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId . <br>
اضغط على الرابط أدناه لتنزيل بطاقتك ببيانات جديدة. <br>
سيكون الرابط نشطًا لمدة 24 ساعة فقط. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1309' AND lang_code='ara';
UPDATE master.template
SET "name"='Success sms to self update demographic data', descr='Success sms to self update demographic data', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails est terminée avec succès le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>
Cliquez sur le lien ci-dessous pour télécharger votre carte avec de nouvelles données. <br>
Le lien ne sera actif que pendant 24 heures. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1309' AND lang_code='fra';
UPDATE master.template
SET "name"='Success sms to self update demographic data', descr='Success sms to self update demographic data', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर सफलतापूर्वक पूरा कर लिया गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
अपने कार्ड को नए डेटा के साथ डाउनलोड करने के लिए नीचे दिए गए लिंक पर क्लिक करें। <br>
लिंक केवल 24 घंटे के लिए सक्रिय रहेगा। <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1309' AND lang_code='hin';
UPDATE master.template
SET "name"='Success sms to self update demographic data', descr='Success sms to self update demographic data', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು $date ರಂದು $time ನಲ್ಲಿ ಯಶಸ್ವಿಯಾಗಿ ಪೂರ್ಣಗೊಂಡಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಹೊಸ ಡೇಟಾದೊಂದಿಗೆ ನಿಮ್ಮ ಕಾರ್ಡ್ ಅನ್ನು ಡೌನ್‌ಲೋಡ್ ಮಾಡಲು ಕೆಳಗಿನ ಲಿಂಕ್ ಅನ್ನು ಕ್ಲಿಕ್ ಮಾಡಿ. <br>
ಲಿಂಕ್ 24 ಗಂಟೆಗಳ ಕಾಲ ಮಾತ್ರ ಸಕ್ರಿಯವಾಗಿರುತ್ತದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1309' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure sms to self update demographic data', descr='Failure sms to self update demographic data', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails émise le $date à $time a échoué. <br>
Votre identifiant d''événement est #$eventId. <br>
Connectez-vous au site Web officiel $trackServiceRequestLink pour plus de détails. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1310' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure sms to self update demographic data', descr='Failure sms to self update demographic data', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$date ರಂದು $time ನಲ್ಲಿ ಸಂಗ್ರಹಿಸಲಾದ $eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಹೆಚ್ಚಿನ ವಿವರಗಳಿಗಾಗಿ ಅಧಿಕೃತ ವೆಬ್‌ಸೈಟ್ $trackServiceRequestLink ಗೆ ಲಾಗ್ ಇನ್ ಮಾಡಿ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1310' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure sms to self update demographic data', descr='Failure sms to self update demographic data', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails raised on $date at $time has failed. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1310' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure sms to self update demographic data', descr='Failure sms to self update demographic data', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
لقد فشل طلبك إلى  $eventDetails الذي تم رفعه في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>
قم بتسجيل الدخول إلى الموقع الرسمي  $trackServiceRequestLink لمزيد من التفاصيل. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1310' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure sms to self update demographic data', descr='Failure sms to self update demographic data', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date पर $time पर उठाया गया विफल हो गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
अधिक जानकारी के लिए आधिकारिक वेबसाइट $trackServiceRequestLink में लॉग इन करें। <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1310' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure sms to self update demographic data', descr='Failure sms to self update demographic data', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$date அன்று $time இல் பெறப்பட்ட $eventDetailsக்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
மேலும் விவரங்களுக்கு அதிகாரப்பூர்வ இணையதளமான $trackServiceRequestLink இல் உள்நுழைக. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1310' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received sms to generate or revoke VID', descr='Request received sms to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم استلام طلبك إلى  $eventDetails في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1311' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received sms to generate or revoke VID', descr='Request received sms to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು $date ರಂದು $time ನಲ್ಲಿ ಸ್ವೀಕರಿಸಲಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1311' AND lang_code='kan';
UPDATE master.template
SET "name"='Request received sms to generate or revoke VID', descr='Request received sms to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetails க்கான உங்கள் கோரிக்கை $date அன்று $time இல் பெறப்பட்டது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1311' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received sms to generate or revoke VID', descr='Request received sms to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर प्राप्त हुआ है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1311' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received sms to generate or revoke VID', descr='Request received sms to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails a été reçue le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1311' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received sms to generate or revoke VID', descr='Request received sms to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails has been received on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1311' AND lang_code='eng';
UPDATE master.template
SET "name"='Success sms to generate or revoke VID', descr='Success sms to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم إكمال طلبك إلى  $eventDetails بنجاح في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1312' AND lang_code='ara';
UPDATE master.template
SET "name"='Success sms to generate or revoke VID', descr='Success sms to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर सफलतापूर्वक पूरा कर लिया गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1312' AND lang_code='hin';
UPDATE master.template
SET "name"='Success sms to generate or revoke VID', descr='Success sms to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು $date ರಂದು $time ನಲ್ಲಿ ಯಶಸ್ವಿಯಾಗಿ ಪೂರ್ಣಗೊಂಡಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1312' AND lang_code='kan';
UPDATE master.template
SET "name"='Success sms to generate or revoke VID', descr='Success sms to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails est terminée avec succès le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1312' AND lang_code='fra';
UPDATE master.template
SET "name"='Success sms to generate or revoke VID', descr='Success sms to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails is completed successfully on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1312' AND lang_code='eng';
UPDATE master.template
SET "name"='Success sms to generate or revoke VID', descr='Success sms to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetailsக்கான உங்கள் கோரிக்கை $date அன்று $time இல் வெற்றிகரமாக நிறைவுற்றது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1312' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure sms to generate or revoke VID', descr='Failure sms to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
لقد فشل طلبك إلى  $eventDetails الذي تم رفعه في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>
قم بتسجيل الدخول إلى الموقع الرسمي  $trackServiceRequestLink لمزيد من التفاصيل. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1313' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure sms to generate or revoke VID', descr='Failure sms to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date पर $time पर उठाया गया विफल हो गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
अधिक जानकारी के लिए आधिकारिक वेबसाइट $trackServiceRequestLink में लॉग इन करें। <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1313' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure sms to generate or revoke VID', descr='Failure sms to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$date ರಂದು $time ನಲ್ಲಿ ಸಂಗ್ರಹಿಸಲಾದ $eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಹೆಚ್ಚಿನ ವಿವರಗಳಿಗಾಗಿ ಅಧಿಕೃತ ವೆಬ್‌ಸೈಟ್ $trackServiceRequestLink ಗೆ ಲಾಗ್ ಇನ್ ಮಾಡಿ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1313' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure sms to generate or revoke VID', descr='Failure sms to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$date அன்று $time இல் பெறப்பட்ட $eventDetailsக்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
மேலும் விவரங்களுக்கு அதிகாரப்பூர்வ இணையதளமான $trackServiceRequestLink இல் உள்நுழைக. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1313' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure sms to generate or revoke VID', descr='Failure sms to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails raised on $date at $time has failed. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1313' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure sms to generate or revoke VID', descr='Failure sms to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails émise le $date à $time a échoué. <br>
Votre identifiant d''événement est #$eventId. <br>
Connectez-vous au site Web officiel $trackServiceRequestLink pour plus de détails. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1313' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received sms to get my UIN card', descr='Request received sms to get my UIN card', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर प्राप्त हुआ है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1314' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received sms to get my UIN card', descr='Request received sms to get my UIN card', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetails க்கான உங்கள் கோரிக்கை $date அன்று $time இல் பெறப்பட்டது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1314' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received sms to get my UIN card', descr='Request received sms to get my UIN card', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم استلام طلبك إلى  $eventDetails في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1314' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received sms to get my UIN card', descr='Request received sms to get my UIN card', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails a été reçue le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1314' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received sms to get my UIN card', descr='Request received sms to get my UIN card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails has been received on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1314' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received sms to get my UIN card', descr='Request received sms to get my UIN card', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು $date ರಂದು $time ನಲ್ಲಿ ಸ್ವೀಕರಿಸಲಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1314' AND lang_code='kan';
UPDATE master.template
SET "name"='Success sms to get my UIN card', descr='Success sms to get my UIN card', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು $date ರಂದು $time ನಲ್ಲಿ ಯಶಸ್ವಿಯಾಗಿ ಪೂರ್ಣಗೊಂಡಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1315' AND lang_code='kan';
UPDATE master.template
SET "name"='Success sms to get my UIN card', descr='Success sms to get my UIN card', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails est terminée avec succès le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1315' AND lang_code='fra';
UPDATE master.template
SET "name"='Success sms to get my UIN card', descr='Success sms to get my UIN card', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم إكمال طلبك إلى  $eventDetails بنجاح في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1315' AND lang_code='ara';
UPDATE master.template
SET "name"='Success sms to get my UIN card', descr='Success sms to get my UIN card', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetailsக்கான உங்கள் கோரிக்கை $date அன்று $time இல் வெற்றிகரமாக நிறைவுற்றது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1315' AND lang_code='tam';
UPDATE master.template
SET "name"='Success sms to get my UIN card', descr='Success sms to get my UIN card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails is completed successfully on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1315' AND lang_code='eng';
UPDATE master.template
SET "name"='Success sms to get my UIN card', descr='Success sms to get my UIN card', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर सफलतापूर्वक पूरा कर लिया गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1315' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure sms to get my UIN card', descr='Failure sms to get my UIN card', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$date ರಂದು $time ನಲ್ಲಿ ಸಂಗ್ರಹಿಸಲಾದ $eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಹೆಚ್ಚಿನ ವಿವರಗಳಿಗಾಗಿ ಅಧಿಕೃತ ವೆಬ್‌ಸೈಟ್ $trackServiceRequestLink ಗೆ ಲಾಗ್ ಇನ್ ಮಾಡಿ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1316' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure sms to get my UIN card', descr='Failure sms to get my UIN card', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
لقد فشل طلبك إلى  $eventDetails الذي تم رفعه في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>
قم بتسجيل الدخول إلى الموقع الرسمي  $trackServiceRequestLink لمزيد من التفاصيل. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1316' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure sms to get my UIN card', descr='Failure sms to get my UIN card', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$date அன்று $time இல் பெறப்பட்ட $eventDetailsக்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
மேலும் விவரங்களுக்கு அதிகாரப்பூர்வ இணையதளமான $trackServiceRequestLink இல் உள்நுழைக. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1316' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure sms to get my UIN card', descr='Failure sms to get my UIN card', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date पर $time पर उठाया गया विफल हो गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
अधिक जानकारी के लिए आधिकारिक वेबसाइट $trackServiceRequestLink में लॉग इन करें। <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1316' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure sms to get my UIN card', descr='Failure sms to get my UIN card', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails émise le $date à $time a échoué. <br>
Votre identifiant d''événement est #$eventId. <br>
Connectez-vous au site Web officiel $trackServiceRequestLink pour plus de détails. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1316' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure sms to get my UIN card', descr='Failure sms to get my UIN card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails raised on $date at $time has failed. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1316' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received sms to verify my phone and email', descr='Request received sms to verify my phone and email', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು $date ರಂದು $time ನಲ್ಲಿ ಸ್ವೀಕರಿಸಲಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1317' AND lang_code='kan';
UPDATE master.template
SET "name"='Request received sms to verify my phone and email', descr='Request received sms to verify my phone and email', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails has been received on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1317' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received sms to verify my phone and email', descr='Request received sms to verify my phone and email', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails a été reçue le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1317' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received sms to verify my phone and email', descr='Request received sms to verify my phone and email', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर प्राप्त हुआ है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1317' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received sms to verify my phone and email', descr='Request received sms to verify my phone and email', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم استلام طلبك إلى  $eventDetails في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1317' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received sms to verify my phone and email', descr='Request received sms to verify my phone and email', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetails க்கான உங்கள் கோரிக்கை $date அன்று $time இல் பெறப்பட்டது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1317' AND lang_code='tam';
UPDATE master.template
SET "name"='Success sms to verify my phone and email', descr='Success sms to verify my phone and email', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails is completed successfully on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1318' AND lang_code='eng';
UPDATE master.template
SET "name"='Success sms to verify my phone and email', descr='Success sms to verify my phone and email', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು $date ರಂದು $time ನಲ್ಲಿ ಯಶಸ್ವಿಯಾಗಿ ಪೂರ್ಣಗೊಂಡಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1318' AND lang_code='kan';
UPDATE master.template
SET "name"='Success sms to verify my phone and email', descr='Success sms to verify my phone and email', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetailsக்கான உங்கள் கோரிக்கை $date அன்று $time இல் வெற்றிகரமாக நிறைவுற்றது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1318' AND lang_code='tam';
UPDATE master.template
SET "name"='Success sms to verify my phone and email', descr='Success sms to verify my phone and email', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर सफलतापूर्वक पूरा कर लिया गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1318' AND lang_code='hin';
UPDATE master.template
SET "name"='Success sms to verify my phone and email', descr='Success sms to verify my phone and email', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم إكمال طلبك إلى  $eventDetails بنجاح في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1318' AND lang_code='ara';
UPDATE master.template
SET "name"='Success sms to verify my phone and email', descr='Success sms to verify my phone and email', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails est terminée avec succès le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1318' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure sms to verify my phone and email', descr='Failure sms to verify my phone and email', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$date அன்று $time இல் பெறப்பட்ட $eventDetailsக்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
மேலும் விவரங்களுக்கு அதிகாரப்பூர்வ இணையதளமான $trackServiceRequestLink இல் உள்நுழைக. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1319' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure sms to verify my phone and email', descr='Failure sms to verify my phone and email', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$date ರಂದು $time ನಲ್ಲಿ ಸಂಗ್ರಹಿಸಲಾದ $eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಹೆಚ್ಚಿನ ವಿವರಗಳಿಗಾಗಿ ಅಧಿಕೃತ ವೆಬ್‌ಸೈಟ್ $trackServiceRequestLink ಗೆ ಲಾಗ್ ಇನ್ ಮಾಡಿ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1319' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure sms to verify my phone and email', descr='Failure sms to verify my phone and email', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails raised on $date at $time has failed. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1319' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure sms to verify my phone and email', descr='Failure sms to verify my phone and email', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date पर $time पर उठाया गया विफल हो गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
अधिक जानकारी के लिए आधिकारिक वेबसाइट $trackServiceRequestLink में लॉग इन करें। <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1319' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure sms to verify my phone and email', descr='Failure sms to verify my phone and email', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
لقد فشل طلبك إلى  $eventDetails الذي تم رفعه في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>
قم بتسجيل الدخول إلى الموقع الرسمي  $trackServiceRequestLink لمزيد من التفاصيل. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1319' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure sms to verify my phone and email', descr='Failure sms to verify my phone and email', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails émise le $date à $time a échoué. <br>
Votre identifiant d''événement est #$eventId. <br>
Connectez-vous au site Web officiel $trackServiceRequestLink pour plus de détails. <br>', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1319' AND lang_code='fra';
UPDATE master.template
SET "name"='Positive purpose to customize and download my card', descr='Positive purpose to customize and download my card', file_format_code='txt', model='velocity', file_txt='वैयक्तिकृत कार्ड का अनुरोध किया गया था', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1344' AND lang_code='hin';
UPDATE master.template
SET "name"='Positive purpose to customize and download my card', descr='Positive purpose to customize and download my card', file_format_code='txt', model='velocity', file_txt='Personalized card was downloaded', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1344' AND lang_code='eng';
UPDATE master.template
SET "name"='Positive purpose to customize and download my card', descr='Positive purpose to customize and download my card', file_format_code='txt', model='velocity', file_txt='تم طلب بطاقة شخصية', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1344' AND lang_code='ara';
UPDATE master.template
SET "name"='Positive purpose to customize and download my card', descr='Positive purpose to customize and download my card', file_format_code='txt', model='velocity', file_txt='Une carte personnalisée a été demandée', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1344' AND lang_code='fra';
UPDATE master.template
SET "name"='Positive purpose to customize and download my card', descr='Positive purpose to customize and download my card', file_format_code='txt', model='velocity', file_txt='ವೈಯಕ್ತಿಕಗೊಳಿಸಿದ ಕಾರ್ಡ್ ಅನ್ನು ವಿನಂತಿಸಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1344' AND lang_code='kan';
UPDATE master.template
SET "name"='Positive purpose to customize and download my card', descr='Positive purpose to customize and download my card', file_format_code='txt', model='velocity', file_txt='தனிப்பயனாக்கப்பட்ட அட்டை கோரப்பட்டது', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1344' AND lang_code='tam';
UPDATE master.template
SET "name"='Negative purpose to customize and download my card', descr='Negative purpose to customize and download my card', file_format_code='txt', model='velocity', file_txt='Tentative de téléchargement de la carte personnalisée', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1345' AND lang_code='fra';
UPDATE master.template
SET "name"='Negative purpose to customize and download my card', descr='Negative purpose to customize and download my card', file_format_code='txt', model='velocity', file_txt='व्यक्तिगत कार्ड डाउनलोड करने का प्रयास किया गया', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1345' AND lang_code='hin';
UPDATE master.template
SET "name"='Negative purpose to customize and download my card', descr='Negative purpose to customize and download my card', file_format_code='txt', model='velocity', file_txt='தனிப்பயனாக்கப்பட்ட அட்டையைப் பதிவிறக்கம் செய்ய முயற்சி மேற்கொள்ளப்பட்டது', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1345' AND lang_code='tam';
UPDATE master.template
SET "name"='Negative purpose to customize and download my card', descr='Negative purpose to customize and download my card', file_format_code='txt', model='velocity', file_txt='An attempt was made to download personalised card', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1345' AND lang_code='eng';
UPDATE master.template
SET "name"='Negative purpose to customize and download my card', descr='Negative purpose to customize and download my card', file_format_code='txt', model='velocity', file_txt='ವೈಯಕ್ತೀಕರಿಸಿದ ಕಾರ್ಡ್ ಅನ್ನು ಡೌನ್‌ಲೋಡ್ ಮಾಡಲು ಪ್ರಯತ್ನಿಸಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1345' AND lang_code='kan';
UPDATE master.template
SET "name"='Negative purpose to customize and download my card', descr='Negative purpose to customize and download my card', file_format_code='txt', model='velocity', file_txt='جرت محاولة لتنزيل بطاقة شخصية', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1345' AND lang_code='ara';
UPDATE master.template
SET "name"='Positive purpose to order a physical card', descr='Positive purpose to order a physical card', file_format_code='txt', model='velocity', file_txt='भौतिक कार्ड का आदेश दिया गया था', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-positive purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1346' AND lang_code='hin';
UPDATE master.template
SET "name"='Positive purpose to order a physical card', descr='Positive purpose to order a physical card', file_format_code='txt', model='velocity', file_txt='Physical card was ordered', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-positive purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1346' AND lang_code='eng';
UPDATE master.template
SET "name"='Positive purpose to order a physical card', descr='Positive purpose to order a physical card', file_format_code='txt', model='velocity', file_txt='ಭೌತಿಕ ಕಾರ್ಡ್ ಅನ್ನು ಆದೇಶಿಸಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-positive purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1346' AND lang_code='kan';
UPDATE master.template
SET "name"='Positive purpose to order a physical card', descr='Positive purpose to order a physical card', file_format_code='txt', model='velocity', file_txt='உடல் அட்டை ஆர்டர் செய்யப்பட்டது', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-positive purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1346' AND lang_code='tam';
UPDATE master.template
SET "name"='Positive purpose to order a physical card', descr='Positive purpose to order a physical card', file_format_code='txt', model='velocity', file_txt='La carte physique a été commandée', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-positive purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1346' AND lang_code='fra';
UPDATE master.template
SET "name"='Positive purpose to order a physical card', descr='Positive purpose to order a physical card', file_format_code='txt', model='velocity', file_txt='تم طلب البطاقة الفعلية', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-positive purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1346' AND lang_code='ara';
UPDATE master.template
SET "name"='Negative purpose to order a physical card', descr='Negative purpose to order a physical card', file_format_code='txt', model='velocity', file_txt='An attempt was made to order a physical card', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-negative purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1347' AND lang_code='eng';
UPDATE master.template
SET "name"='Negative purpose to order a physical card', descr='Negative purpose to order a physical card', file_format_code='txt', model='velocity', file_txt='Tentative de commande d''une carte physique', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-negative purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1347' AND lang_code='fra';
UPDATE master.template
SET "name"='Negative purpose to order a physical card', descr='Negative purpose to order a physical card', file_format_code='txt', model='velocity', file_txt='भौतिक कार्ड ऑर्डर करने का प्रयास किया गया', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-negative purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1347' AND lang_code='hin';
UPDATE master.template
SET "name"='Negative purpose to order a physical card', descr='Negative purpose to order a physical card', file_format_code='txt', model='velocity', file_txt='ಭೌತಿಕ ಕಾರ್ಡ್ ಅನ್ನು ಆರ್ಡರ್ ಮಾಡಲು ಪ್ರಯತ್ನಿಸಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-negative purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1347' AND lang_code='kan';
UPDATE master.template
SET "name"='Negative purpose to order a physical card', descr='Negative purpose to order a physical card', file_format_code='txt', model='velocity', file_txt='உடல் அட்டையை ஆர்டர் செய்ய முயற்சி மேற்கொள்ளப்பட்டது', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-negative purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1347' AND lang_code='tam';
UPDATE master.template
SET "name"='Negative purpose to order a physical card', descr='Negative purpose to order a physical card', file_format_code='txt', model='velocity', file_txt='جرت محاولة لطلب بطاقة فعلية', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-negative purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1347' AND lang_code='ara';
UPDATE master.template
SET "name"='Positive purpose to share my credential with a partner', descr='Positive purpose to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='ಪಾಲುದಾರರೊಂದಿಗೆ ಡೇಟಾವನ್ನು ಹಂಚಿಕೊಳ್ಳಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1348' AND lang_code='kan';
UPDATE master.template
SET "name"='Positive purpose to share my credential with a partner', descr='Positive purpose to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='பங்குதாரருடன் தரவு பகிரப்பட்டது', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1348' AND lang_code='tam';
UPDATE master.template
SET "name"='Positive purpose to share my credential with a partner', descr='Positive purpose to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='डेटा एक भागीदार के साथ साझा किया गया था', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1348' AND lang_code='hin';
UPDATE master.template
SET "name"='Positive purpose to share my credential with a partner', descr='Positive purpose to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Les données ont été partagées avec un partenaire', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1348' AND lang_code='fra';
UPDATE master.template
SET "name"='Positive purpose to share my credential with a partner', descr='Positive purpose to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='تمت مشاركة البيانات مع شريك', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1348' AND lang_code='ara';
UPDATE master.template
SET "name"='Positive purpose to share my credential with a partner', descr='Positive purpose to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Data was shared with a partner', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1348' AND lang_code='eng';
UPDATE master.template
SET "name"='Negative purpose to share my credential with a partner', descr='Negative purpose to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='جرت محاولة لمشاركة البيانات مع شريك', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1349' AND lang_code='ara';
UPDATE master.template
SET "name"='Negative purpose to share my credential with a partner', descr='Negative purpose to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='An attempt was made to share data with a partner', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1349' AND lang_code='eng';
UPDATE master.template
SET "name"='Negative purpose to share my credential with a partner', descr='Negative purpose to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Tentative de partage de données avec un partenaire', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1349' AND lang_code='fra';
UPDATE master.template
SET "name"='Negative purpose to share my credential with a partner', descr='Negative purpose to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='एक भागीदार के साथ डेटा साझा करने का प्रयास किया गया', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1349' AND lang_code='hin';
UPDATE master.template
SET "name"='Negative purpose to share my credential with a partner', descr='Negative purpose to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='ಪಾಲುದಾರರೊಂದಿಗೆ ಡೇಟಾವನ್ನು ಹಂಚಿಕೊಳ್ಳಲು ಪ್ರಯತ್ನಿಸಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1349' AND lang_code='kan';
UPDATE master.template
SET "name"='Negative purpose to share my credential with a partner', descr='Negative purpose to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='ஒரு கூட்டாளருடன் தரவைப் பகிர முயற்சி மேற்கொள்ளப்பட்டது', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1349' AND lang_code='tam';
UPDATE master.template
SET "name"='Positive purpose to lock/unlock various authentication types', descr='Positive purpose to lock/unlock various authentication types', file_format_code='txt', model='velocity', file_txt='تم قفل $ authType
تم إلغاء تأمين $ authType', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1350' AND lang_code='ara';
UPDATE master.template
SET "name"='Positive purpose to lock/unlock various authentication types', descr='Positive purpose to lock/unlock various authentication types', file_format_code='txt', model='velocity', file_txt='$authType लॉक किया गया था
$authType अनलॉक किया गया था', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1350' AND lang_code='hin';
UPDATE master.template
SET "name"='Positive purpose to lock/unlock various authentication types', descr='Positive purpose to lock/unlock various authentication types', file_format_code='txt', model='velocity', file_txt='$authType authentication is $status', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1350' AND lang_code='eng';
UPDATE master.template
SET "name"='Positive purpose to lock/unlock various authentication types', descr='Positive purpose to lock/unlock various authentication types', file_format_code='txt', model='velocity', file_txt='$authType était verrouillé
$authType a été déverrouillé', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1350' AND lang_code='fra';
UPDATE master.template
SET "name"='Positive purpose to lock/unlock various authentication types', descr='Positive purpose to lock/unlock various authentication types', file_format_code='txt', model='velocity', file_txt='$authType ಅನ್ನು ಲಾಕ್ ಮಾಡಲಾಗಿದೆ
$authType ಅನ್ನು ಅನ್‌ಲಾಕ್ ಮಾಡಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1350' AND lang_code='kan';
UPDATE master.template
SET "name"='Positive purpose to lock/unlock various authentication types', descr='Positive purpose to lock/unlock various authentication types', file_format_code='txt', model='velocity', file_txt='$authType பூட்டப்பட்டது
$authType திறக்கப்பட்டது', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1350' AND lang_code='tam';
UPDATE master.template
SET "name"='Negative purpose to lock/unlock various authentication types', descr='Negative purpose to lock/unlock various authentication types', file_format_code='txt', model='velocity', file_txt='$authType ஐப் பூட்ட முயற்சி செய்யப்பட்டது
$authType ஐ திறக்க முயற்சி செய்யப்பட்டது', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1351' AND lang_code='tam';
UPDATE master.template
SET "name"='Negative purpose to lock/unlock various authentication types', descr='Negative purpose to lock/unlock various authentication types', file_format_code='txt', model='velocity', file_txt='Une tentative a été faite pour verrouiller $authType
Une tentative a été faite pour déverrouiller $authType', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1351' AND lang_code='fra';
UPDATE master.template
SET "name"='Negative purpose to lock/unlock various authentication types', descr='Negative purpose to lock/unlock various authentication types', file_format_code='txt', model='velocity', file_txt='$authType . को लॉक करने का प्रयास किया गया
$authType . को अनलॉक करने का प्रयास किया गया', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1351' AND lang_code='hin';
UPDATE master.template
SET "name"='Negative purpose to lock/unlock various authentication types', descr='Negative purpose to lock/unlock various authentication types', file_format_code='txt', model='velocity', file_txt='$authType ಅನ್ನು ಲಾಕ್ ಮಾಡಲು ಪ್ರಯತ್ನಿಸಲಾಗಿದೆ
$authType ಅನ್ನು ಅನ್‌ಲಾಕ್ ಮಾಡಲು ಪ್ರಯತ್ನಿಸಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1351' AND lang_code='kan';
UPDATE master.template
SET "name"='Negative purpose to lock/unlock various authentication types', descr='Negative purpose to lock/unlock various authentication types', file_format_code='txt', model='velocity', file_txt='جرت محاولة لتأمين $ authType
جرت محاولة لإلغاء تأمين $ authType', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1351' AND lang_code='ara';
UPDATE master.template
SET "name"='Negative purpose to lock/unlock various authentication types', descr='Negative purpose to lock/unlock various authentication types', file_format_code='txt', model='velocity', file_txt='An attempt was made to lock/unlock authentication types', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1351' AND lang_code='eng';
UPDATE master.template
SET "name"='Positive Purpose to self update demographic data', descr='Positive Purpose to self update demographic data', file_format_code='txt', model='velocity', file_txt='தரவு புதுப்பிக்கப்பட்டது (எ.கா.: (பெயர் மற்றும் பாலினம் புதுப்பிக்கப்பட்டது)', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1352' AND lang_code='tam';
UPDATE master.template
SET "name"='Positive Purpose to self update demographic data', descr='Positive Purpose to self update demographic data', file_format_code='txt', model='velocity', file_txt='ಡೇಟಾವನ್ನು ನವೀಕರಿಸಲಾಗಿದೆ (ಉದಾ.: (ಹೆಸರು ಮತ್ತು ಲಿಂಗವನ್ನು ನವೀಕರಿಸಲಾಗಿದೆ)', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1352' AND lang_code='kan';
UPDATE master.template
SET "name"='Positive Purpose to self update demographic data', descr='Positive Purpose to self update demographic data', file_format_code='txt', model='velocity', file_txt='Data was updated successfully', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1352' AND lang_code='eng';
UPDATE master.template
SET "name"='Positive Purpose to self update demographic data', descr='Positive Purpose to self update demographic data', file_format_code='txt', model='velocity', file_txt='डेटा अपडेट किया गया था (उदा.: (नाम और लिंग अपडेट किए गए थे)', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1352' AND lang_code='hin';
UPDATE master.template
SET "name"='Positive Purpose to self update demographic data', descr='Positive Purpose to self update demographic data', file_format_code='txt', model='velocity', file_txt='Les données ont été mises à jour (par exemple : (le nom et le sexe ont été mis à jour)', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1352' AND lang_code='fra';
UPDATE master.template
SET "name"='Positive Purpose to self update demographic data', descr='Positive Purpose to self update demographic data', file_format_code='txt', model='velocity', file_txt='تم تحديث البيانات (على سبيل المثال: (تم تحديث الاسم والجنس)', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1352' AND lang_code='ara';
UPDATE master.template
SET "name"='Negative Purpose to self update demographic data', descr='Negative Purpose to self update demographic data', file_format_code='txt', model='velocity', file_txt='Tentative de mise à jour des données', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1353' AND lang_code='fra';
UPDATE master.template
SET "name"='Negative Purpose to self update demographic data', descr='Negative Purpose to self update demographic data', file_format_code='txt', model='velocity', file_txt='جرت محاولة لتحديث البيانات', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1353' AND lang_code='ara';
UPDATE master.template
SET "name"='Negative Purpose to self update demographic data', descr='Negative Purpose to self update demographic data', file_format_code='txt', model='velocity', file_txt='डेटा अपडेट करने का प्रयास किया गया', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1353' AND lang_code='hin';
UPDATE master.template
SET "name"='Negative Purpose to self update demographic data', descr='Negative Purpose to self update demographic data', file_format_code='txt', model='velocity', file_txt='ಡೇಟಾವನ್ನು ನವೀಕರಿಸಲು ಪ್ರಯತ್ನಿಸಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1353' AND lang_code='kan';
UPDATE master.template
SET "name"='Negative Purpose to self update demographic data', descr='Negative Purpose to self update demographic data', file_format_code='txt', model='velocity', file_txt='தரவைப் புதுப்பிக்கும் முயற்சி மேற்கொள்ளப்பட்டது', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1353' AND lang_code='tam';
UPDATE master.template
SET "name"='Negative Purpose to self update demographic data', descr='Negative Purpose to self update demographic data', file_format_code='txt', model='velocity', file_txt='An attempt was made to update data', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1353' AND lang_code='eng';
UPDATE master.template
SET "name"='Positive Purpose  to generate or revoke VIDs', descr='Positive Purpose  to generate or revoke VIDs', file_format_code='txt', model='velocity', file_txt='1. VID जनरेट किया गया
2. वीआईडी ​​रद्द कर दिया गया था', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1354' AND lang_code='hin';
UPDATE master.template
SET "name"='Positive Purpose  to generate or revoke VIDs', descr='Positive Purpose  to generate or revoke VIDs', file_format_code='txt', model='velocity', file_txt='1. ವಿಐಡಿ ರಚಿಸಲಾಗಿದೆ
2. ವಿಐಡಿಯನ್ನು ಹಿಂಪಡೆಯಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1354' AND lang_code='kan';
UPDATE master.template
SET "name"='Positive Purpose  to generate or revoke VIDs', descr='Positive Purpose  to generate or revoke VIDs', file_format_code='txt', model='velocity', file_txt='1. விஐடி உருவாக்கப்பட்டது
2. விஐடி ரத்து செய்யப்பட்டது', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1354' AND lang_code='tam';
UPDATE master.template
SET "name"='Positive Purpose  to generate or revoke VIDs', descr='Positive Purpose  to generate or revoke VIDs', file_format_code='txt', model='velocity', file_txt='VID was $actionPerformed', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1354' AND lang_code='eng';
UPDATE master.template
SET "name"='Positive Purpose  to generate or revoke VIDs', descr='Positive Purpose  to generate or revoke VIDs', file_format_code='txt', model='velocity', file_txt='1. تم إنشاء VID
2. تم إبطال VID', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1354' AND lang_code='ara';
UPDATE master.template
SET "name"='Positive Purpose  to generate or revoke VIDs', descr='Positive Purpose  to generate or revoke VIDs', file_format_code='txt', model='velocity', file_txt='1. Le VID a été généré
2. Le VID a été révoqué', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1354' AND lang_code='fra';
UPDATE master.template
SET "name"='Negative Purpose to generate or revoke VIDs', descr='Negative Purpose to generate or revoke VIDs', file_format_code='txt', model='velocity', file_txt='1. VID ಅನ್ನು ರಚಿಸಲು ಪ್ರಯತ್ನಿಸಲಾಗಿದೆ
2. VID ಹಿಂಪಡೆಯಲು ಪ್ರಯತ್ನಿಸಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1355' AND lang_code='kan';
UPDATE master.template
SET "name"='Negative Purpose to generate or revoke VIDs', descr='Negative Purpose to generate or revoke VIDs', file_format_code='txt', model='velocity', file_txt='1. விஐடியை உருவாக்கும் முயற்சி மேற்கொள்ளப்பட்டது
2. விஐடியை திரும்பப் பெற முயற்சி செய்யப்பட்டது', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1355' AND lang_code='tam';
UPDATE master.template
SET "name"='Negative Purpose to generate or revoke VIDs', descr='Negative Purpose to generate or revoke VIDs', file_format_code='txt', model='velocity', file_txt='1. VID उत्पन्न करने का प्रयास किया गया था
2. VID को रद्द करने का प्रयास किया गया था', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1355' AND lang_code='hin';
UPDATE master.template
SET "name"='Negative Purpose to generate or revoke VIDs', descr='Negative Purpose to generate or revoke VIDs', file_format_code='txt', model='velocity', file_txt='1. Une tentative a été faite pour générer un VID
2. Une tentative a été faite pour révoquer le VID', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1355' AND lang_code='fra';
UPDATE master.template
SET "name"='Negative Purpose to generate or revoke VIDs', descr='Negative Purpose to generate or revoke VIDs', file_format_code='txt', model='velocity', file_txt='1. جرت محاولة لإنشاء VID
2. جرت محاولة لإلغاء VID', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1355' AND lang_code='ara';
UPDATE master.template
SET "name"='Negative Purpose to generate or revoke VIDs', descr='Negative Purpose to generate or revoke VIDs', file_format_code='txt', model='velocity', file_txt='An attempt was made to $actionPerformed VID', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1355' AND lang_code='eng';
UPDATE master.template
SET "name"='Positive purpose to get my UIN card', descr='Positive purpose to get my UIN card', file_format_code='txt', model='velocity', file_txt='1. La carte UIN a été téléchargée
2. L''état de l''AID a été affiché', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1356' AND lang_code='fra';
UPDATE master.template
SET "name"='Positive purpose to get my UIN card', descr='Positive purpose to get my UIN card', file_format_code='txt', model='velocity', file_txt='1. تم تنزيل بطاقة UIN
2. تم عرض حالة AID', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1356' AND lang_code='ara';
UPDATE master.template
SET "name"='Positive purpose to get my UIN card', descr='Positive purpose to get my UIN card', file_format_code='txt', model='velocity', file_txt='1. UIN கார்டு பதிவிறக்கப்பட்டது
2. எய்டின் நிலை காட்டப்பட்டது', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1356' AND lang_code='tam';
UPDATE master.template
SET "name"='Positive purpose to get my UIN card', descr='Positive purpose to get my UIN card', file_format_code='txt', model='velocity', file_txt='1. UIN ಕಾರ್ಡ್ ಅನ್ನು ಡೌನ್‌ಲೋಡ್ ಮಾಡಲಾಗಿದೆ
2. AID ಯ ಸ್ಥಿತಿಯನ್ನು ಪ್ರದರ್ಶಿಸಲಾಯಿತು', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1356' AND lang_code='kan';
UPDATE master.template
SET "name"='Positive purpose to get my UIN card', descr='Positive purpose to get my UIN card', file_format_code='txt', model='velocity', file_txt='1. यूआईएन कार्ड डाउनलोड किया गया
2. सहायता की स्थिति प्रदर्शित की गई', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1356' AND lang_code='hin';
UPDATE master.template
SET "name"='Positive purpose to get my UIN card', descr='Positive purpose to get my UIN card', file_format_code='txt', model='velocity', file_txt='UIN card was downloaded', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1356' AND lang_code='eng';
UPDATE master.template
SET "name"='Negative purpose to get my UIN card', descr='Negative purpose to get my UIN card', file_format_code='txt', model='velocity', file_txt='1. UIN ಕಾರ್ಡ್ ಡೌನ್‌ಲೋಡ್ ಮಾಡಲು ಪ್ರಯತ್ನಿಸಲಾಗಿದೆ
2. AID ಸ್ಥಿತಿಯನ್ನು ವೀಕ್ಷಿಸಲು ಪ್ರಯತ್ನಿಸಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1357' AND lang_code='kan';
UPDATE master.template
SET "name"='Negative purpose to get my UIN card', descr='Negative purpose to get my UIN card', file_format_code='txt', model='velocity', file_txt='1. UIN கார்டைப் பதிவிறக்கும் முயற்சி மேற்கொள்ளப்பட்டது
2. எய்டின் நிலையைப் பார்க்க முயற்சி செய்யப்பட்டது', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1357' AND lang_code='tam';
UPDATE master.template
SET "name"='Negative purpose to get my UIN card', descr='Negative purpose to get my UIN card', file_format_code='txt', model='velocity', file_txt='1. Tentative de téléchargement de la carte UIN
2. Une tentative a été faite pour afficher l''état de l''AID', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1357' AND lang_code='fra';
UPDATE master.template
SET "name"='Negative purpose to get my UIN card', descr='Negative purpose to get my UIN card', file_format_code='txt', model='velocity', file_txt='An attempt was made to download UIN card', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1357' AND lang_code='eng';
UPDATE master.template
SET "name"='Negative purpose to get my UIN card', descr='Negative purpose to get my UIN card', file_format_code='txt', model='velocity', file_txt='1. جرت محاولة لتنزيل بطاقة UIN
2. جرت محاولة لعرض حالة المعونة الأمريكية', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1357' AND lang_code='ara';
UPDATE master.template
SET "name"='Negative purpose to get my UIN card', descr='Negative purpose to get my UIN card', file_format_code='txt', model='velocity', file_txt='1. यूआईएन कार्ड डाउनलोड करने का प्रयास किया गया
2. AID की स्थिति देखने का प्रयास किया गया था', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1357' AND lang_code='hin';
UPDATE master.template
SET "name"='Positive purpose to verify my phone number and email ID', descr='Positive purpose to verify my phone number and email ID', file_format_code='txt', model='velocity', file_txt='1. Le numéro de téléphone a été vérifié
2. L''identifiant de messagerie a été vérifié', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1358' AND lang_code='fra';
UPDATE master.template
SET "name"='Positive purpose to verify my phone number and email ID', descr='Positive purpose to verify my phone number and email ID', file_format_code='txt', model='velocity', file_txt='$channel was verified', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1358' AND lang_code='eng';
UPDATE master.template
SET "name"='Positive purpose to verify my phone number and email ID', descr='Positive purpose to verify my phone number and email ID', file_format_code='txt', model='velocity', file_txt='1. फोन नंबर सत्यापित किया गया था
2. ईमेल आईडी सत्यापित किया गया था', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1358' AND lang_code='hin';
UPDATE master.template
SET "name"='Positive purpose to verify my phone number and email ID', descr='Positive purpose to verify my phone number and email ID', file_format_code='txt', model='velocity', file_txt='1. تم التحقق من رقم الهاتف
2. تم التحقق من معرف البريد الإلكتروني', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1358' AND lang_code='ara';
UPDATE master.template
SET "name"='Positive purpose to verify my phone number and email ID', descr='Positive purpose to verify my phone number and email ID', file_format_code='txt', model='velocity', file_txt='1. ಫೋನ್ ಸಂಖ್ಯೆಯನ್ನು ಪರಿಶೀಲಿಸಲಾಗಿದೆ
2. ಇಮೇಲ್ ಐಡಿಯನ್ನು ಪರಿಶೀಲಿಸಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1358' AND lang_code='kan';
UPDATE master.template
SET "name"='Positive purpose to verify my phone number and email ID', descr='Positive purpose to verify my phone number and email ID', file_format_code='txt', model='velocity', file_txt='1. தொலைபேசி எண் சரிபார்க்கப்பட்டது
2. மின்னஞ்சல் ஐடி சரிபார்க்கப்பட்டது', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1358' AND lang_code='tam';
UPDATE master.template
SET "name"='Negative purpose to verify my phone number and email ID', descr='Negative purpose to verify my phone number and email ID', file_format_code='txt', model='velocity', file_txt='1. फ़ोन नंबर सत्यापित करने का प्रयास किया गया
2. ईमेल आईडी सत्यापित करने का प्रयास किया गया था', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1359' AND lang_code='hin';
UPDATE master.template
SET "name"='Negative purpose to verify my phone number and email ID', descr='Negative purpose to verify my phone number and email ID', file_format_code='txt', model='velocity', file_txt='1. தொலைபேசி எண்ணைச் சரிபார்க்க முயற்சி செய்யப்பட்டது
2. மின்னஞ்சல் ஐடியை சரிபார்க்க முயற்சி செய்யப்பட்டது', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1359' AND lang_code='tam';
UPDATE master.template
SET "name"='Negative purpose to verify my phone number and email ID', descr='Negative purpose to verify my phone number and email ID', file_format_code='txt', model='velocity', file_txt='1. ಫೋನ್ ಸಂಖ್ಯೆಯನ್ನು ಪರಿಶೀಲಿಸಲು ಪ್ರಯತ್ನಿಸಲಾಗಿದೆ
2. ಇಮೇಲ್ ಐಡಿಯನ್ನು ಪರಿಶೀಲಿಸಲು ಪ್ರಯತ್ನಿಸಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1359' AND lang_code='kan';
UPDATE master.template
SET "name"='Negative purpose to verify my phone number and email ID', descr='Negative purpose to verify my phone number and email ID', file_format_code='txt', model='velocity', file_txt='An attempt was made to verify $channel', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1359' AND lang_code='eng';
UPDATE master.template
SET "name"='Negative purpose to verify my phone number and email ID', descr='Negative purpose to verify my phone number and email ID', file_format_code='txt', model='velocity', file_txt='1. جرت محاولة للتحقق من رقم الهاتف
2. جرت محاولة للتحقق من معرف البريد الإلكتروني', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1359' AND lang_code='ara';
UPDATE master.template
SET "name"='Negative purpose to verify my phone number and email ID', descr='Negative purpose to verify my phone number and email ID', file_format_code='txt', model='velocity', file_txt='1. Une tentative a été faite pour vérifier le numéro de téléphone
2. Une tentative a été faite pour vérifier l''identifiant de messagerie', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1359' AND lang_code='fra';
UPDATE master.template
SET "name"='Success summary to customize and download my card', descr='Success summary to customize and download my card', file_format_code='txt', model='velocity', file_txt='$attributes वाला वैयक्तिकृत कार्ड सफलतापूर्वक जनरेट किया गया और पंजीकृत ईमेल आईडी और/या फ़ोन नंबर पर भेजा गया', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1360' AND lang_code='hin';
UPDATE master.template
SET "name"='Success summary to customize and download my card', descr='Success summary to customize and download my card', file_format_code='txt', model='velocity', file_txt='Personalised card was downloaded successfully', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1360' AND lang_code='eng';
UPDATE master.template
SET "name"='Success summary to customize and download my card', descr='Success summary to customize and download my card', file_format_code='txt', model='velocity', file_txt='تم إنشاء بطاقة شخصية بسما$attributes بنجاح وتم إرسالها إلى معرف البريد الإلكتروني المسجل و / أو رقم الهاتف', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1360' AND lang_code='ara';
UPDATE master.template
SET "name"='Success summary to customize and download my card', descr='Success summary to customize and download my card', file_format_code='txt', model='velocity', file_txt='La carte personnalisée avec les $attributes a été générée avec succès et envoyée à l''adresse e-mail et/ou au numéro de téléphone enregistrés', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1360' AND lang_code='fra';
UPDATE master.template
SET "name"='Success summary to customize and download my card', descr='Success summary to customize and download my card', file_format_code='txt', model='velocity', file_txt='$attributes ಜೊತೆಗೆ ವೈಯಕ್ತಿಕಗೊಳಿಸಿದ ಕಾರ್ಡ್ ಅನ್ನು ಯಶಸ್ವಿಯಾಗಿ ರಚಿಸಲಾಗಿದೆ ಮತ್ತು ನೋಂದಾಯಿತ ಇಮೇಲ್ ID ಮತ್ತು/ಅಥವಾ ಫೋನ್ ಸಂಖ್ಯೆಗೆ ಕಳುಹಿಸಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1360' AND lang_code='kan';
UPDATE master.template
SET "name"='Success summary to customize and download my card', descr='Success summary to customize and download my card', file_format_code='txt', model='velocity', file_txt='$attributes கொண்ட தனிப்பயனாக்கப்பட்ட அட்டை வெற்றிகரமாக உருவாக்கப்பட்டு, பதிவுசெய்யப்பட்ட மின்னஞ்சல் ஐடி மற்றும்/அல்லது தொலைபேசி எண்ணுக்கு அனுப்பப்பட்டது', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1360' AND lang_code='tam';
UPDATE master.template
SET "name"='Success summary to order a physical card', descr='Success summary to order a physical card', file_format_code='txt', model='velocity', file_txt='Order for a physical card has been placed successfully with the partner', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1361' AND lang_code='eng';
UPDATE master.template
SET "name"='Success summary to order a physical card', descr='Success summary to order a physical card', file_format_code='txt', model='velocity', file_txt='பார்ட்னரிடம் உடல் அட்டைக்கான ஆர்டர் வெற்றிகரமாக வைக்கப்பட்டுள்ளது', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1361' AND lang_code='tam';
UPDATE master.template
SET "name"='Success summary to order a physical card', descr='Success summary to order a physical card', file_format_code='txt', model='velocity', file_txt='La commande d''une carte physique a été passée avec succès auprès du partenaire', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1361' AND lang_code='fra';
UPDATE master.template
SET "name"='Success summary to order a physical card', descr='Success summary to order a physical card', file_format_code='txt', model='velocity', file_txt='ಪಾಲುದಾರರೊಂದಿಗೆ ಭೌತಿಕ ಕಾರ್ಡ್‌ಗಾಗಿ ಆರ್ಡರ್ ಅನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಇರಿಸಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1361' AND lang_code='kan';
UPDATE master.template
SET "name"='Success summary to order a physical card', descr='Success summary to order a physical card', file_format_code='txt', model='velocity', file_txt='फिजिकल कार्ड के लिए ऑर्डर पार्टनर को सफलतापूर्वक दे दिया गया है', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1361' AND lang_code='hin';
UPDATE master.template
SET "name"='Success summary to order a physical card', descr='Success summary to order a physical card', file_format_code='txt', model='velocity', file_txt='تم تقديم طلب للحصول على بطاقة فعلية بنجاح مع الشريك', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1361' AND lang_code='ara';
UPDATE master.template
SET "name"='Success summary to share my credential with a partner', descr='Success summary to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='ನಿಮ್ಮ $attributes ಗುಣಲಕ್ಷಣಗಳನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಹಂಚಿಕೊಳ್ಳಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1362' AND lang_code='kan';
UPDATE master.template
SET "name"='Success summary to share my credential with a partner', descr='Success summary to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='تمت مشاركة سمات $attributes الخاصة بك بنجاح', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1362' AND lang_code='ara';
UPDATE master.template
SET "name"='Success summary to share my credential with a partner', descr='Success summary to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='உங்கள் $attributes புகள் வெற்றிகரமாகப் பகிரப்பட்டன', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1362' AND lang_code='tam';
UPDATE master.template
SET "name"='Success summary to share my credential with a partner', descr='Success summary to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Your data was shared successfully', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1362' AND lang_code='eng';
UPDATE master.template
SET "name"='Success summary to share my credential with a partner', descr='Success summary to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Vos $attributes ont été partagés avec succès', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1362' AND lang_code='fra';
UPDATE master.template
SET "name"='Success summary to share my credential with a partner', descr='Success summary to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='आपकी $attributes सफलतापूर्वक साझा की ग', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1362' AND lang_code='hin';
UPDATE master.template
SET "name"='Success summary to lock/unlock various authentication types', descr='Success summary to lock/unlock various authentication types', file_format_code='txt', model='velocity', file_txt='$authType authentication is $status', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1363' AND lang_code='eng';
UPDATE master.template
SET "name"='Success summary to lock/unlock various authentication types', descr='Success summary to lock/unlock various authentication types', file_format_code='txt', model='velocity', file_txt='ನಿಮ್ಮ $authType ಅನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಲಾಕ್ ಮಾಡಲಾಗಿದೆ
ನಿಮ್ಮ $authType ಅನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಅನ್‌ಲಾಕ್ ಮಾಡಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1363' AND lang_code='kan';
UPDATE master.template
SET "name"='Success summary to lock/unlock various authentication types', descr='Success summary to lock/unlock various authentication types', file_format_code='txt', model='velocity', file_txt='Votre $authType a été verrouillé avec succès
Votre $authType a été déverrouillé avec succès', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1363' AND lang_code='fra';
UPDATE master.template
SET "name"='Success summary to lock/unlock various authentication types', descr='Success summary to lock/unlock various authentication types', file_format_code='txt', model='velocity', file_txt='م قفل $authType الخاص بك بنجاح
تم إلغاء قفل  $authType الخاص بك بنجاح', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1363' AND lang_code='ara';
UPDATE master.template
SET "name"='Success summary to lock/unlock various authentication types', descr='Success summary to lock/unlock various authentication types', file_format_code='txt', model='velocity', file_txt='आपका $authType सफलतापूर्वक लॉक कर दिया गया था
आपका $authType सफलतापूर्वक अनलॉक हो गया था', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1363' AND lang_code='hin';
UPDATE master.template
SET "name"='Success summary to lock/unlock various authentication types', descr='Success summary to lock/unlock various authentication types', file_format_code='txt', model='velocity', file_txt='உங்கள் $authType வெற்றிகரமாக பூட்டப்பட்டது
உங்கள் $authType வெற்றிகரமாக திறக்கப்பட்டது', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1363' AND lang_code='tam';
UPDATE master.template
SET "name"='Success summary to self update demographic data', descr='Success summary to self update demographic data', file_format_code='txt', model='velocity', file_txt='Data was updated successfully', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1364' AND lang_code='eng';
UPDATE master.template
SET "name"='Success summary to self update demographic data', descr='Success summary to self update demographic data', file_format_code='txt', model='velocity', file_txt='आपकी $attributes सफलतापूर्वक अपडेट की गईं और पंजीकृत ईमेल आईडी और/या फोन नंबर पर भेज दी गईं', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1364' AND lang_code='hin';
UPDATE master.template
SET "name"='Success summary to self update demographic data', descr='Success summary to self update demographic data', file_format_code='txt', model='velocity', file_txt='Vos $attributes ont été mis à jour avec succès et envoyés à l''identifiant de messagerie et/ou au numéro de téléphone enregistrés', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1364' AND lang_code='fra';
UPDATE master.template
SET "name"='Success summary to self update demographic data', descr='Success summary to self update demographic data', file_format_code='txt', model='velocity', file_txt='உங்கள் $attributesக்கூறுகள் வெற்றிகரமாக புதுப்பிக்கப்பட்டு பதிவு செய்யப்பட்ட மின்னஞ்சல் ஐடி மற்றும்/அல்லது ஃபோன் எண்ணுக்கு அனுப்பப்பட்டது', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1364' AND lang_code='tam';
UPDATE master.template
SET "name"='Success summary to self update demographic data', descr='Success summary to self update demographic data', file_format_code='txt', model='velocity', file_txt='ನಿಮ್ಮ $attributes ಯಶಸ್ವಿಯಾಗಿ ನವೀಕರಿಸಲಾಗಿದೆ ಮತ್ತು ನೋಂದಾಯಿತ ಇಮೇಲ್ ID ಮತ್ತು/ಅಥವಾ ಫೋನ್ ಸಂಖ್ಯೆಗೆ ಕಳುಹಿಸಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1364' AND lang_code='kan';
UPDATE master.template
SET "name"='Success summary to self update demographic data', descr='Success summary to self update demographic data', file_format_code='txt', model='velocity', file_txt='تم تحديث سمات $attributes بنجاح وتم إرسالها إلى معرف البريد الإلكتروني المسجل و / أو رقم الهاتف', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1364' AND lang_code='ara';
UPDATE master.template
SET "name"='Success summary to generate or revoke VIDs', descr='Success summary to generate or revoke VIDs', file_format_code='txt', model='velocity', file_txt='$vidType VID *masked VID* was $actionPerformed successfully', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1365' AND lang_code='eng';
UPDATE master.template
SET "name"='Success summary to generate or revoke VIDs', descr='Success summary to generate or revoke VIDs', file_format_code='txt', model='velocity', file_txt='$vidType VID *नकाबपोश VID* सफलतापूर्वक जनरेट किया गया
$vidType VID *नकाबपोश VID* को सफलतापूर्वक निरस्त कर दिया गया', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1365' AND lang_code='hin';
UPDATE master.template
SET "name"='Success summary to generate or revoke VIDs', descr='Success summary to generate or revoke VIDs', file_format_code='txt', model='velocity', file_txt='$vidType VID *VID masqué* a été généré avec succès
$vidType VID *masked VID* a été révoqué avec succès', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1365' AND lang_code='fra';
UPDATE master.template
SET "name"='Success summary to generate or revoke VIDs', descr='Success summary to generate or revoke VIDs', file_format_code='txt', model='velocity', file_txt='تم إنشاء  vidType$ VID * مقنع VID * بنجاح
تم إبطال vidType$ VID * المقنع VID * بنجاح', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1365' AND lang_code='ara';
UPDATE master.template
SET "name"='Success summary to generate or revoke VIDs', descr='Success summary to generate or revoke VIDs', file_format_code='txt', model='velocity', file_txt='$vidType VID *ಮಾಸ್ಕ್ಡ್ VID* ಅನ್ನು ಯಶಸ್ವಿಯಾಗಿ ರಚಿಸಲಾಗಿದೆ
$vidType VID *ಮಾಸ್ಕ್ ಮಾಡಿದ VID* ಅನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಹಿಂಪಡೆಯಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1365' AND lang_code='kan';
UPDATE master.template
SET "name"='Success summary to generate or revoke VIDs', descr='Success summary to generate or revoke VIDs', file_format_code='txt', model='velocity', file_txt='$vidType VID *மாஸ்க் செய்யப்பட்ட VID* வெற்றிகரமாக உருவாக்கப்பட்டது
$vidType VID *மாஸ்க் செய்யப்பட்ட VID* வெற்றிகரமாக திரும்பப் பெறப்பட்டது', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1365' AND lang_code='tam';
UPDATE master.template
SET "name"='Success summary to get my UIN card', descr='Success summary to get my UIN card', file_format_code='txt', model='velocity', file_txt='UIN கார்டு பதிவு செய்யப்பட்ட மின்னஞ்சல் ஐடி மற்றும்/அல்லது தொலைபேசி எண்ணுக்கு வெற்றிகரமாக அனுப்பப்பட்டது', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1366' AND lang_code='tam';
UPDATE master.template
SET "name"='Success summary to get my UIN card', descr='Success summary to get my UIN card', file_format_code='txt', model='velocity', file_txt='ನೋಂದಾಯಿತ ಇಮೇಲ್ ಐಡಿ ಮತ್ತು/ಅಥವಾ ಫೋನ್ ಸಂಖ್ಯೆಗೆ UIN ಕಾರ್ಡ್ ಅನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಕಳುಹಿಸಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1366' AND lang_code='kan';
UPDATE master.template
SET "name"='Success summary to get my UIN card', descr='Success summary to get my UIN card', file_format_code='txt', model='velocity', file_txt='यूआईएन कार्ड सफलतापूर्वक पंजीकृत ईमेल आईडी और/या फोन नंबर पर भेजा गया था', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1366' AND lang_code='hin';
UPDATE master.template
SET "name"='Success summary to get my UIN card', descr='Success summary to get my UIN card', file_format_code='txt', model='velocity', file_txt='La carte UIN a été envoyée avec succès à l''adresse e-mail et/ou au numéro de téléphone enregistrés', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1366' AND lang_code='fra';
UPDATE master.template
SET "name"='Success summary to get my UIN card', descr='Success summary to get my UIN card', file_format_code='txt', model='velocity', file_txt='UIN card was downloaded successfully', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1366' AND lang_code='eng';
UPDATE master.template
SET "name"='Success summary to get my UIN card', descr='Success summary to get my UIN card', file_format_code='txt', model='velocity', file_txt='تم إرسال بطاقة UIN بنجاح إلى معرف البريد الإلكتروني المسجل و / أو رقم الهاتف', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1366' AND lang_code='ara';
UPDATE master.template
SET "name"='Success summary to verify my phone and email', descr='Success summary to verify my phone and email', file_format_code='txt', model='velocity', file_txt='உங்கள் தொலைபேசி எண் வெற்றிகரமாகச் சரிபார்க்கப்பட்டது
உங்கள் மின்னஞ்சல் ஐடி வெற்றிகரமாகச் சரிபார்க்கப்பட்டது', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1367' AND lang_code='tam';
UPDATE master.template
SET "name"='Success summary to verify my phone and email', descr='Success summary to verify my phone and email', file_format_code='txt', model='velocity', file_txt='تم التحقق من رقم هاتفك بنجاح
تم التحقق من معرف البريد الإلكتروني الخاص بك بنجاح', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1367' AND lang_code='ara';
UPDATE master.template
SET "name"='Success summary to verify my phone and email', descr='Success summary to verify my phone and email', file_format_code='txt', model='velocity', file_txt='ನಿಮ್ಮ ಫೋನ್ ಸಂಖ್ಯೆಯನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಪರಿಶೀಲಿಸಲಾಗಿದೆ
ನಿಮ್ಮ ಇಮೇಲ್ ಐಡಿಯನ್ನು ಯಶಸ್ವಿಯಾಗಿ ಪರಿಶೀಲಿಸಲಾಗಿದೆ', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1367' AND lang_code='kan';
UPDATE master.template
SET "name"='Success summary to verify my phone and email', descr='Success summary to verify my phone and email', file_format_code='txt', model='velocity', file_txt='Your $channel was successfully verified', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1367' AND lang_code='eng';
UPDATE master.template
SET "name"='Success summary to verify my phone and email', descr='Success summary to verify my phone and email', file_format_code='txt', model='velocity', file_txt='आपका फ़ोन नंबर सफलतापूर्वक सत्यापित किया गया था
आपकी ईमेल आईडी को सफलतापूर्व', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1367' AND lang_code='hin';
UPDATE master.template
SET "name"='Success summary to verify my phone and email', descr='Success summary to verify my phone and email', file_format_code='txt', model='velocity', file_txt='Votre numéro de téléphone a été vérifié avec succès
Votre identifiant de messagerie a été vérifié avec succès', module_id='10006', module_name='Resident Services', template_typ_code='verify-my-phone-email-success-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1367' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received email to customize and download my card', descr='Request received email to customize and download my card', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails a été reçue le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1368' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received email to customize and download my card', descr='Request received email to customize and download my card', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم استلام طلبك إلى  $eventDetails في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1368' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received email to customize and download my card', descr='Request received email to customize and download my card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails has been received on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1368' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received email to customize and download my card', descr='Request received email to customize and download my card', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetails க்கான உங்கள் கோரிக்கை $date அன்று $time இல் பெறப்பட்டது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1368' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received email to customize and download my card', descr='Request received email to customize and download my card', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು $date ರಂದು $time ನಲ್ಲಿ ಸ್ವೀಕರಿಸಲಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1368' AND lang_code='kan';
UPDATE master.template
SET "name"='Request received email to customize and download my card', descr='Request received email to customize and download my card', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर प्राप्त हुआ है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1368' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received email subject to order a physical card', descr='Request received email subject to order a physical card', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1369' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received email subject to order a physical card', descr='Request received email subject to order a physical card', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1369' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received email subject to order a physical card', descr='Request received email subject to order a physical card', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1369' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received email subject to order a physical card', descr='Request received email subject to order a physical card', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1369' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received email subject to order a physical card', descr='Request received email subject to order a physical card', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1369' AND lang_code='kan';
UPDATE master.template
SET "name"='Request received email subject to order a physical card', descr='Request received email subject to order a physical card', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='order-a-physical-card-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1369' AND lang_code='fra';
UPDATE master.template
SET "name"='Success email subject to share my credential with a partner', descr='Success email subject to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1370' AND lang_code='tam';
UPDATE master.template
SET "name"='Success email subject to share my credential with a partner', descr='Success email subject to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1370' AND lang_code='kan';
UPDATE master.template
SET "name"='Success email subject to share my credential with a partner', descr='Success email subject to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1370' AND lang_code='fra';
UPDATE master.template
SET "name"='Success email subject to share my credential with a partner', descr='Success email subject to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1370' AND lang_code='ara';
UPDATE master.template
SET "name"='Success email subject to share my credential with a partner', descr='Success email subject to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1370' AND lang_code='eng';
UPDATE master.template
SET "name"='Success email subject to share my credential with a partner', descr='Success email subject to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1370' AND lang_code='hin';
UPDATE master.template
SET "name"='Success email to share my credential with a partner', descr='Success email to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$Partner உடனான $eventDetailsக்கான உங்கள் கோரிக்கை $date அன்று $time இல் வெற்றிகரமாக நிறைவடைந்தது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1371' AND lang_code='tam';
UPDATE master.template
SET "name"='Success email to share my credential with a partner', descr='Success email to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$ पार्टनर के साथ $eventDetails के लिए आपका अनुरोध $date पर $time पर सफलतापूर्वक पूरा हो गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1371' AND lang_code='hin';
UPDATE master.template
SET "name"='Success email to share my credential with a partner', descr='Success email to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails avec $partner est terminée avec succès le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1371' AND lang_code='fra';
UPDATE master.template
SET "name"='Success email to share my credential with a partner', descr='Success email to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails ($partner) is completed successfully on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1371' AND lang_code='eng';
UPDATE master.template
SET "name"='Success email to share my credential with a partner', descr='Success email to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم إكمال طلبك إلى  $eventDetails مع  $partner بنجاح في  $date عند  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>
يمكنك تتبع حالة المعاملة باستخدام معرف المعاملة  $transactionID على الرابط  $trackingLink', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1371' AND lang_code='ara';
UPDATE master.template
SET "name"='Success email to share my credential with a partner', descr='Success email to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$Partner ಜೊತೆಗೆ $eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು $date ರಂದು $time ನಲ್ಲಿ ಯಶಸ್ವಿಯಾಗಿ ಪೂರ್ಣಗೊಳಿಸಲಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1371' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure email to share my credential with a partner', descr='Failure email to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails raised on $date at $time has failed. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1372' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure email to share my credential with a partner', descr='Failure email to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
لقد فشل طلبك إلى  $eventDetails الذي تم رفعه في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>
قم بتسجيل الدخول إلى الموقع الرسمي  $trackServiceRequestLink لمزيد من التفاصيل. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1372' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure email to share my credential with a partner', descr='Failure email to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails émise le $date à $time a échoué. <br>
Votre identifiant d''événement est #$eventId. <br>
Connectez-vous au site Web officiel $trackServiceRequestLink pour plus de détails. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1372' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure email to share my credential with a partner', descr='Failure email to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date पर $time पर उठाया गया विफल हो गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
अधिक जानकारी के लिए आधिकारिक वेबसाइट $trackServiceRequestLink में लॉग इन करें। <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1372' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure email to share my credential with a partner', descr='Failure email to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$date ರಂದು $time ನಲ್ಲಿ ಸಂಗ್ರಹಿಸಲಾದ $eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಹೆಚ್ಚಿನ ವಿವರಗಳಿಗಾಗಿ ಅಧಿಕೃತ ವೆಬ್‌ಸೈಟ್ $trackServiceRequestLink ಗೆ ಲಾಗ್ ಇನ್ ಮಾಡಿ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1372' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure email to share my credential with a partner', descr='Failure email to share my credential with a partner', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$date அன்று $time இல் பெறப்பட்ட $eventDetailsக்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
மேலும் விவரங்களுக்கு அதிகாரப்பூர்வ இணையதளமான $trackServiceRequestLink இல் உள்நுழைக. <br>', module_id='10006', module_name='Resident Services', template_typ_code='share-cred-with-partner-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1372' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received email subject to lock/unlock authentication', descr='Request received email subject to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1373' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received email subject to lock/unlock authentication', descr='Request received email subject to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1373' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received email subject to lock/unlock authentication', descr='Request received email subject to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1373' AND lang_code='kan';
UPDATE master.template
SET "name"='Request received email subject to lock/unlock authentication', descr='Request received email subject to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1373' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received email subject to lock/unlock authentication', descr='Request received email subject to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1373' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received email subject to lock/unlock authentication', descr='Request received email subject to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1373' AND lang_code='eng';
UPDATE master.template
SET "name"='Success email subject to lock/unlock authentication', descr='Success email subject to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1374' AND lang_code='eng';
UPDATE master.template
SET "name"='Success email subject to lock/unlock authentication', descr='Success email subject to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1374' AND lang_code='fra';
UPDATE master.template
SET "name"='Success email subject to lock/unlock authentication', descr='Success email subject to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1374' AND lang_code='tam';
UPDATE master.template
SET "name"='Success email subject to lock/unlock authentication', descr='Success email subject to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1374' AND lang_code='kan';
UPDATE master.template
SET "name"='Success email subject to lock/unlock authentication', descr='Success email subject to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1374' AND lang_code='hin';
UPDATE master.template
SET "name"='Success email subject to lock/unlock authentication', descr='Success email subject to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1374' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure email subject to lock/unlock authentication', descr='Failure email subject to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1375' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure email subject to lock/unlock authentication', descr='Failure email subject to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1375' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure email subject to lock/unlock authentication', descr='Failure email subject to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1375' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure email subject to lock/unlock authentication', descr='Failure email subject to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1375' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure email subject to lock/unlock authentication', descr='Failure email subject to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1375' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure email subject to lock/unlock authentication', descr='Failure email subject to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1375' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received email to lock/unlock authentication', descr='Request received email to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು $date ರಂದು $time ನಲ್ಲಿ ಸ್ವೀಕರಿಸಲಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1376' AND lang_code='kan';
UPDATE master.template
SET "name"='Request received email to lock/unlock authentication', descr='Request received email to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails has been received on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1376' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received email to lock/unlock authentication', descr='Request received email to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails a été reçue le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1376' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received email to lock/unlock authentication', descr='Request received email to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetails க்கான உங்கள் கோரிக்கை $date அன்று $time இல் பெறப்பட்டது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1376' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received email to lock/unlock authentication', descr='Request received email to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم استلام طلبك إلى  $eventDetails في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1376' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received email to lock/unlock authentication', descr='Request received email to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर प्राप्त हुआ है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1376' AND lang_code='hin';
UPDATE master.template
SET "name"='Success email to lock/unlock authentication', descr='Success email to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर सफलतापूर्वक पूरा कर लिया गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1377' AND lang_code='hin';
UPDATE master.template
SET "name"='Success email to lock/unlock authentication', descr='Success email to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails est terminée avec succès le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1377' AND lang_code='fra';
UPDATE master.template
SET "name"='Success email to lock/unlock authentication', descr='Success email to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم إكمال طلبك إلى  $eventDetails بنجاح في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1377' AND lang_code='ara';
UPDATE master.template
SET "name"='Success email to lock/unlock authentication', descr='Success email to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails is completed successfully on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1377' AND lang_code='eng';
UPDATE master.template
SET "name"='Success email to lock/unlock authentication', descr='Success email to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು $date ರಂದು $time ನಲ್ಲಿ ಯಶಸ್ವಿಯಾಗಿ ಪೂರ್ಣಗೊಂಡಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1377' AND lang_code='kan';
UPDATE master.template
SET "name"='Success email to lock/unlock authentication', descr='Success email to lock/unlock authentication', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetailsக்கான உங்கள் கோரிக்கை $date அன்று $time இல் வெற்றிகரமாக நிறைவுற்றது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='lock-unlock-auth-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1377' AND lang_code='tam';
UPDATE master.template
SET "name"='Success email subject to self update demographic data', descr='Success email subject to self update demographic data', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1378' AND lang_code='kan';
UPDATE master.template
SET "name"='Success email subject to self update demographic data', descr='Success email subject to self update demographic data', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1378' AND lang_code='hin';
UPDATE master.template
SET "name"='Success email subject to self update demographic data', descr='Success email subject to self update demographic data', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1378' AND lang_code='eng';
UPDATE master.template
SET "name"='Success email subject to self update demographic data', descr='Success email subject to self update demographic data', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1378' AND lang_code='ara';
UPDATE master.template
SET "name"='Success email subject to self update demographic data', descr='Success email subject to self update demographic data', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1378' AND lang_code='fra';
UPDATE master.template
SET "name"='Success email subject to self update demographic data', descr='Success email subject to self update demographic data', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1378' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure email subject to self update demographic data', descr='Failure email subject to self update demographic data', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1379' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure email subject to self update demographic data', descr='Failure email subject to self update demographic data', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1379' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure email subject to self update demographic data', descr='Failure email subject to self update demographic data', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1379' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure email subject to self update demographic data', descr='Failure email subject to self update demographic data', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1379' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure email subject to self update demographic data', descr='Failure email subject to self update demographic data', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1379' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure email subject to self update demographic data', descr='Failure email subject to self update demographic data', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1379' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received email to self update demographic data', descr='Request received email to self update demographic data', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails a été reçue le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1380' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received email to self update demographic data', descr='Request received email to self update demographic data', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم استلام طلبك إلى  $eventDetails في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1380' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received email to self update demographic data', descr='Request received email to self update demographic data', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು $date ರಂದು $time ನಲ್ಲಿ ಸ್ವೀಕರಿಸಲಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1380' AND lang_code='kan';
UPDATE master.template
SET "name"='Request received email to self update demographic data', descr='Request received email to self update demographic data', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर प्राप्त हुआ है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1380' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received email to self update demographic data', descr='Request received email to self update demographic data', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetails க்கான உங்கள் கோரிக்கை $date அன்று $time இல் பெறப்பட்டது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1380' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received email to self update demographic data', descr='Request received email to self update demographic data', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails has been received on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1380' AND lang_code='eng';
UPDATE master.template
SET "name"='Success email to self update demographic data', descr='Success email to self update demographic data', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು $date ರಂದು $time ನಲ್ಲಿ ಯಶಸ್ವಿಯಾಗಿ ಪೂರ್ಣಗೊಂಡಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಹೊಸ ಡೇಟಾದೊಂದಿಗೆ ನಿಮ್ಮ ಕಾರ್ಡ್ ಅನ್ನು ಡೌನ್‌ಲೋಡ್ ಮಾಡಲು ಕೆಳಗಿನ ಲಿಂಕ್ ಅನ್ನು ಕ್ಲಿಕ್ ಮಾಡಿ. <br>
ಲಿಂಕ್ 24 ಗಂಟೆಗಳ ಕಾಲ ಮಾತ್ರ ಸಕ್ರಿಯವಾಗಿರುತ್ತದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1381' AND lang_code='kan';
UPDATE master.template
SET "name"='Success email to self update demographic data', descr='Success email to self update demographic data', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails is completed successfully on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1381' AND lang_code='eng';
UPDATE master.template
SET "name"='Success email to self update demographic data', descr='Success email to self update demographic data', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم إكمال طلبك إلى  $eventDetails بنجاح في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId . <br>
اضغط على الرابط أدناه لتنزيل بطاقتك ببيانات جديدة. <br>
سيكون الرابط نشطًا لمدة 24 ساعة فقط. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1381' AND lang_code='ara';
UPDATE master.template
SET "name"='Success email to self update demographic data', descr='Success email to self update demographic data', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails est terminée avec succès le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>
Cliquez sur le lien ci-dessous pour télécharger votre carte avec de nouvelles données. <br>
Le lien ne sera actif que pendant 24 heures. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1381' AND lang_code='fra';
UPDATE master.template
SET "name"='Success email to self update demographic data', descr='Success email to self update demographic data', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर सफलतापूर्वक पूरा कर लिया गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
अपने कार्ड को नए डेटा के साथ डाउनलोड करने के लिए नीचे दिए गए लिंक पर क्लिक करें। <br>
लिंक केवल 24 घंटे के लिए सक्रिय रहेगा। <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1381' AND lang_code='hin';
UPDATE master.template
SET "name"='Success email to self update demographic data', descr='Success email to self update demographic data', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetailsக்கான உங்கள் கோரிக்கை $date அன்று $time இல் வெற்றிகரமாக நிறைவுற்றது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
புதிய டேட்டாவுடன் உங்கள் கார்டைப் பதிவிறக்க கீழே உள்ள இணைப்பைக் கிளிக் செய்யவும். <br>
இணைப்பு 24 மணிநேரம் மட்டுமே செயலில் இருக்கும். <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1381' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure email to self update demographic data', descr='Failure email to self update demographic data', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date पर $time पर उठाया गया विफल हो गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
अधिक जानकारी के लिए आधिकारिक वेबसाइट $trackServiceRequestLink में लॉग इन करें। <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1382' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure email to self update demographic data', descr='Failure email to self update demographic data', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$date அன்று $time இல் பெறப்பட்ட $eventDetailsக்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
மேலும் விவரங்களுக்கு அதிகாரப்பூர்வ இணையதளமான $trackServiceRequestLink இல் உள்நுழைக. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1382' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure email to self update demographic data', descr='Failure email to self update demographic data', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails émise le $date à $time a échoué. <br>
Votre identifiant d''événement est #$eventId. <br>
Connectez-vous au site Web officiel $trackServiceRequestLink pour plus de détails. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1382' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure email to self update demographic data', descr='Failure email to self update demographic data', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
لقد فشل طلبك إلى  $eventDetails الذي تم رفعه في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>
قم بتسجيل الدخول إلى الموقع الرسمي  $trackServiceRequestLink لمزيد من التفاصيل. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1382' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure email to self update demographic data', descr='Failure email to self update demographic data', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$date ರಂದು $time ನಲ್ಲಿ ಸಂಗ್ರಹಿಸಲಾದ $eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಹೆಚ್ಚಿನ ವಿವರಗಳಿಗಾಗಿ ಅಧಿಕೃತ ವೆಬ್‌ಸೈಟ್ $trackServiceRequestLink ಗೆ ಲಾಗ್ ಇನ್ ಮಾಡಿ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1382' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure email to self update demographic data', descr='Failure email to self update demographic data', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails raised on $date at $time has failed. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='update-demo-data-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1382' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received email subject to generate or revoke VID', descr='Request received email subject to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1383' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received email subject to generate or revoke VID', descr='Request received email subject to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1383' AND lang_code='tam';
UPDATE master.template
SET "name"='Request received email subject to generate or revoke VID', descr='Request received email subject to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1383' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received email subject to generate or revoke VID', descr='Request received email subject to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1383' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received email subject to generate or revoke VID', descr='Request received email subject to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1383' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received email subject to generate or revoke VID', descr='Request received email subject to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1383' AND lang_code='kan';
UPDATE master.template
SET "name"='Success email subject to generate or revoke VID', descr='Success email subject to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1384' AND lang_code='ara';
UPDATE master.template
SET "name"='Success email subject to generate or revoke VID', descr='Success email subject to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1384' AND lang_code='tam';
UPDATE master.template
SET "name"='Success email subject to generate or revoke VID', descr='Success email subject to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1384' AND lang_code='kan';
UPDATE master.template
SET "name"='Success email subject to generate or revoke VID', descr='Success email subject to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1384' AND lang_code='hin';
UPDATE master.template
SET "name"='Success email subject to generate or revoke VID', descr='Success email subject to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1384' AND lang_code='eng';
UPDATE master.template
SET "name"='Success email subject to generate or revoke VID', descr='Success email subject to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1384' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure email subject to generate or revoke VID', descr='Failure email subject to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1385' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure email subject to generate or revoke VID', descr='Failure email subject to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1385' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure email subject to generate or revoke VID', descr='Failure email subject to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1385' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure email subject to generate or revoke VID', descr='Failure email subject to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1385' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure email subject to generate or revoke VID', descr='Failure email subject to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1385' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure email subject to generate or revoke VID', descr='Failure email subject to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1385' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received email to generate or revoke VID', descr='Request received email to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails a été reçue le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1386' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received email to generate or revoke VID', descr='Request received email to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर प्राप्त हुआ है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1386' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received email to generate or revoke VID', descr='Request received email to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯನ್ನು $date ರಂದು $time ನಲ್ಲಿ ಸ್ವೀಕರಿಸಲಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1386' AND lang_code='kan';
UPDATE master.template
SET "name"='Request received email to generate or revoke VID', descr='Request received email to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails has been received on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1386' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received email to generate or revoke VID', descr='Request received email to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم استلام طلبك إلى  $eventDetails في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1386' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received email to generate or revoke VID', descr='Request received email to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetails க்கான உங்கள் கோரிக்கை $date அன்று $time இல் பெறப்பட்டது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1386' AND lang_code='tam';
UPDATE master.template
SET "name"='Success email to generate or revoke VID', descr='Success email to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails is completed successfully on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1387' AND lang_code='eng';
UPDATE master.template
SET "name"='Success email to generate or revoke VID', descr='Success email to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು $date ರಂದು $time ನಲ್ಲಿ ಯಶಸ್ವಿಯಾಗಿ ಪೂರ್ಣಗೊಂಡಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1387' AND lang_code='kan';
UPDATE master.template
SET "name"='Success email to generate or revoke VID', descr='Success email to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$eventDetailsக்கான உங்கள் கோரிக்கை $date அன்று $time இல் வெற்றிகரமாக நிறைவுற்றது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1387' AND lang_code='tam';
UPDATE master.template
SET "name"='Success email to generate or revoke VID', descr='Success email to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date को $time पर सफलतापूर्वक पूरा कर लिया गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1387' AND lang_code='hin';
UPDATE master.template
SET "name"='Success email to generate or revoke VID', descr='Success email to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails est terminée avec succès le $date à $time. <br>
Votre identifiant d''événement est #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1387' AND lang_code='fra';
UPDATE master.template
SET "name"='Success email to generate or revoke VID', descr='Success email to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
تم إكمال طلبك إلى  $eventDetails بنجاح في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1387' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received email subject to get my UIN card', descr='Request received email subject to get my UIN card', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1388' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received email subject to get my UIN card', descr='Request received email subject to get my UIN card', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1388' AND lang_code='kan';
UPDATE master.template
SET "name"='Request received email subject to get my UIN card', descr='Request received email subject to get my UIN card', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1388' AND lang_code='hin';
UPDATE master.template
SET "name"='Request received email subject to get my UIN card', descr='Request received email subject to get my UIN card', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1388' AND lang_code='fra';
UPDATE master.template
SET "name"='Request received email subject to get my UIN card', descr='Request received email subject to get my UIN card', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1388' AND lang_code='ara';
UPDATE master.template
SET "name"='Request received email subject to get my UIN card', descr='Request received email subject to get my UIN card', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1388' AND lang_code='tam';
UPDATE master.template
SET "name"='Success email subject to get my UIN card', descr='Success email subject to get my UIN card', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1389' AND lang_code='ara';
UPDATE master.template
SET "name"='Success email subject to get my UIN card', descr='Success email subject to get my UIN card', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1389' AND lang_code='fra';
UPDATE master.template
SET "name"='Success email subject to get my UIN card', descr='Success email subject to get my UIN card', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1389' AND lang_code='tam';
UPDATE master.template
SET "name"='Success email subject to get my UIN card', descr='Success email subject to get my UIN card', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1389' AND lang_code='kan';
UPDATE master.template
SET "name"='Success email subject to get my UIN card', descr='Success email subject to get my UIN card', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1389' AND lang_code='hin';
UPDATE master.template
SET "name"='Success email subject to get my UIN card', descr='Success email subject to get my UIN card', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='get-my-uin-card-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1389' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure email subject to customize and download my card', descr='Failure email subject to customize and download my card', file_format_code='txt', model='velocity', file_txt='ಸ್ವೀಕೃತಿ: $eventDetails | ಈವೆಂಟ್ ಐಡಿ: $eventId | ಸ್ಥಿತಿ:  $status', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1390' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure email subject to customize and download my card', descr='Failure email subject to customize and download my card', file_format_code='txt', model='velocity', file_txt='Accusé de réception : $eventDetails | ID d''événement : $eventId | Statut : $status', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1390' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure email subject to customize and download my card', descr='Failure email subject to customize and download my card', file_format_code='txt', model='velocity', file_txt='إقرار: $eventDetails | معرف الحدث: $eventId | الحالة: $status', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1390' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure email subject to customize and download my card', descr='Failure email subject to customize and download my card', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1390' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure email subject to customize and download my card', descr='Failure email subject to customize and download my card', file_format_code='txt', model='velocity', file_txt='ஒப்புகை: $eventDetails | நிகழ்வு ஐடி: $eventId | நிலை: $status', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1390' AND lang_code='tam';
UPDATE master.template
SET "name"='Failure email subject to customize and download my card', descr='Failure email subject to customize and download my card', file_format_code='txt', model='velocity', file_txt='पावती: $eventDetails | इवेंट आईडी: $eventId | स्थिति: $status', module_id='10006', module_name='Resident Services', template_typ_code='cust-and-down-my-card-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1390' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure email to generate or revoke VID', descr='Failure email to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
$eventDetails के लिए आपका अनुरोध $date पर $time पर उठाया गया विफल हो गया है। <br>
आपकी इवेंट आईडी #$eventId है। <br>
अधिक जानकारी के लिए आधिकारिक वेबसाइट $trackServiceRequestLink में लॉग इन करें। <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1391' AND lang_code='hin';
UPDATE master.template
SET "name"='Failure email to generate or revoke VID', descr='Failure email to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Votre demande à $eventDetails émise le $date à $time a échoué. <br>
Votre identifiant d''événement est #$eventId. <br>
Connectez-vous au site Web officiel $trackServiceRequestLink pour plus de détails. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1391' AND lang_code='fra';
UPDATE master.template
SET "name"='Failure email to generate or revoke VID', descr='Failure email to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
$date ರಂದು $time ನಲ್ಲಿ ಸಂಗ್ರಹಿಸಲಾದ $eventDetails ಗೆ ನಿಮ್ಮ ವಿನಂತಿಯು ವಿಫಲವಾಗಿದೆ. <br>
ನಿಮ್ಮ ಈವೆಂಟ್ ಐಡಿ #$eventId ಆಗಿದೆ. <br>
ಹೆಚ್ಚಿನ ವಿವರಗಳಿಗಾಗಿ ಅಧಿಕೃತ ವೆಬ್‌ಸೈಟ್ $trackServiceRequestLink ಗೆ ಲಾಗ್ ಇನ್ ಮಾಡಿ. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1391' AND lang_code='kan';
UPDATE master.template
SET "name"='Failure email to generate or revoke VID', descr='Failure email to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='عزيزي  $name ، <br>
لقد فشل طلبك إلى  $eventDetails الذي تم رفعه في  $date في  $time. <br>
معرف الحدث الخاص بك هو  #$eventId. <br>
قم بتسجيل الدخول إلى الموقع الرسمي  $trackServiceRequestLink لمزيد من التفاصيل. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1391' AND lang_code='ara';
UPDATE master.template
SET "name"='Failure email to generate or revoke VID', descr='Failure email to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails raised on $date at $time has failed. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1391' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure email to generate or revoke VID', descr='Failure email to generate or revoke VID', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
$date அன்று $time இல் பெறப்பட்ட $eventDetailsக்கான உங்கள் கோரிக்கை தோல்வியடைந்தது. <br>
உங்கள் நிகழ்வு ஐடி #$eventId. <br>
மேலும் விவரங்களுக்கு அதிகாரப்பூர்வ இணையதளமான $trackServiceRequestLink இல் உள்நுழைக. <br>', module_id='10006', module_name='Resident Services', template_typ_code='gen-or-revoke-vid-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1391' AND lang_code='tam';
UPDATE master.template
SET "name"='List of supporting documents', descr='List of supporting documents', file_format_code='html', model='velocity', file_txt='<html>

<head>

</head>

<body lang=EN-US style=''word-wrap:break-word''>

<div class=WordSection1>

<p class=MsoNormal align=center style=''text-align:center''><span lang=EN-IN><img
width=200 height=100 id="Picture 2"
src="https://mosip.io/images/logo.png" alt="MOSIP"></span></p>	

<p class=MsoNormal align=center style=''text-align:center''><b><span
style=''font-size:16.0pt;line-height:107%;font-family:"Arial",sans-serif;
color:#1D1C1D;background:yellow''>Supporting documents</span></b></p>

<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0
 style=''border-collapse:collapse;border:none''>
 <tr>
  <td width=623 colspan=2 valign=top style=''width:467.5pt;border:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoNormal align=center style=''margin-bottom:0in;text-align:center;
  line-height:normal''><b><span style=''font-size:14.0pt;font-family:"Arial",sans-serif;
  color:#1D1C1D;background:aqua''>POI (</span></b><b><span style=''font-size:
  14.0pt;background:aqua''>Proof of Identity)</span></b></p>
  <p class=MsoNormal align=center style=''margin-bottom:0in;text-align:center;
  line-height:normal''><b><span style=''font-size:14.0pt;background:white''>&nbsp;</span></b></p>
  </td>
 </tr>
 <tr style=''height:35.85pt''>
  <td width=217 valign=top style=''width:162.8pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt;height:35.85pt''>
  <p class=MsoNormal align=center style=''margin-bottom:0in;text-align:center;
  line-height:normal''><b><span style=''font-size:11.5pt;font-family:"Arial",sans-serif;
  color:#1D1C1D;background:white''>The POI documents specification:</span></b></p>
  </td>
  <td width=406 valign=top style=''width:304.7pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt;height:35.85pt''>
  <p class=MsoListParagraphCxSpFirst style=''margin-bottom:0in;text-indent:-.25in;
  line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>1. It should contain the Full Name and a clear Photo </p>
  <p class=MsoListParagraphCxSpLast style=''margin-bottom:0in;text-indent:-.25in;
  line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>2. It should be valid for at least the next 6 months</p>
  </td>
 </tr>
 <tr>
  <td width=217 valign=top style=''width:162.8pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoNormal align=center style=''margin-bottom:0in;text-align:center;
  line-height:normal''><b><span style=''font-size:11.5pt;font-family:"Arial",sans-serif;
  color:#1D1C1D;background:white''>Acceptable document:</span></b></p>
  <p class=MsoNormal align=center style=''margin-bottom:0in;text-align:center;
  line-height:normal''><span style=''font-size:16.0pt;font-family:"Arial",sans-serif;
  color:#1D1C1D;background:white''>&nbsp;</span></p>
  </td>
  <td width=406 valign=top style=''width:304.7pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraphCxSpFirst style=''margin-bottom:0in;text-indent:-.25in;
  line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>1. Passport </p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:
  -.25in;line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>2. PAN Card/ e-PAN </p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:
  -.25in;line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>3. Ration / PDS Photo Card</p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:
  -.25in;line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>4. Voter ID/ e-Voter ID </p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:
  -.25in;line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>5. Driving License </p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:
  -.25in;line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>6. Service photo identity card issued by Central Govt./ State Govt./ UT
  Govt./ PSU/ Banks </p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:
  -.25in;line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>7. Pensioner Photo Card/Freedom Fighter Photo Card </p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:
  -.25in;line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>8. Disability ID Card/ handicapped medical certificate issued by the
  respective Central/ State/ UT Governments</p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:
  -.25in;line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>9. Bank Pass Book having name and Photograph Cross Stamped by bank
  official</p>
  <p class=MsoListParagraphCxSpLast style=''margin-bottom:0in;text-indent:-.25in;
  line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>10. Valid Visa along with Foreign Passport</p>
  </td>
 </tr>
</table>

<p class=MsoNormal><span style=''font-size:16.0pt;line-height:107%;font-family:
"Arial",sans-serif;color:#1D1C1D;background:white''>&nbsp;</span></p>

<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0
 style=''border-collapse:collapse;border:none''>
 <tr>
  <td width=623 colspan=2 valign=top style=''width:467.5pt;border:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoNormal align=center style=''margin-bottom:0in;text-align:center;
  line-height:normal''><b><span style=''font-size:14.0pt;font-family:"Arial",sans-serif;
  color:#1D1C1D;background:aqua''>POA (Proof of Address)</span></b></p>
  <p class=MsoNormal align=center style=''margin-bottom:0in;text-align:center;
  line-height:normal''><b><span style=''font-size:14.0pt;font-family:"Arial",sans-serif;
  color:#1D1C1D;background:white''>&nbsp;</span></b></p>
  </td>
 </tr>
 <tr style=''height:35.3pt''>
  <td width=217 valign=top style=''width:162.8pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt;height:35.3pt''>
  <p class=MsoNormal align=center style=''margin-bottom:0in;text-align:center;
  line-height:normal''><b><span style=''font-size:11.5pt;font-family:"Arial",sans-serif;
  color:#1D1C1D;background:white''>The POA documents specification:</span></b></p>
  </td>
  <td width=406 valign=top style=''width:304.7pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt;height:35.3pt''>
  <p class=MsoListParagraphCxSpFirst style=''margin-bottom:0in;text-indent:-.25in;
  line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
  </span>1. It should contain Full Name and full address </p>
  <p class=MsoListParagraphCxSpLast style=''margin-bottom:0in;text-indent:-.25in;
  line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
  </span>2. It should be valid for at least the next 6 months</p>
  </td>
 </tr>
 <tr>
  <td width=217 valign=top style=''width:162.8pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoNormal align=center style=''margin-bottom:0in;text-align:center;
  line-height:normal''><b><span style=''font-size:11.5pt;font-family:"Arial",sans-serif;
  color:#1D1C1D;background:white''>Acceptable document:</span></b></p>
  <p class=MsoNormal align=center style=''margin-bottom:0in;text-align:center;
  line-height:normal''><span style=''font-size:16.0pt;font-family:"Arial",sans-serif;
  color:#1D1C1D;background:white''>&nbsp;</span></p>
  </td>
  <td width=406 valign=top style=''width:304.7pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraphCxSpFirst style=''margin-bottom:0in;text-indent:-.25in;
  line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
  </span>1. Passport/ Passport of Spouse/ Passport of Parents (in case of Minor)</p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:-.25in;
  line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
  </span>2. Bank Statement (with Bank stamp &amp; signature of bank official)/ Passbook/ Post
  Office Account Statement/ Passbook</p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:-.25in;
  line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
  </span>3. Ration Card</p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:-.25in;
  line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
  </span>4. Voter ID/ e-Voter ID</p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:-.25in;
  line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
  </span>5. Driving License</p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:-.25in;line-height:normal''><span
  style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>6. Service
  photo identity card issued by PSU/ Banks/ State/ Central Governments</p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:-.25in;line-height:normal''><span
  style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>7. Pensioner
  Card/ Freedom Fighter Card</p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:-.25in;line-height:normal''><span
  style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>8. Kissan
  Passbook </p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:-.25in;line-height:normal''><span
  style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>9. CGHS/
  ECHS/ ESIC/ Medi-Claim Card with Photo issued by State/ Central Govts./ PSUs </p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:-.25in;line-height:normal''><span
  style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>10. Disability ID Card/
  handicapped medical certificate issued by the respective State/ UT
  Governments/ Administrations/ Central Govt. </p>
  <p class=MsoListParagraphCxSpLast style=''margin-bottom:0in;text-indent:-.25in;line-height:normal''><span
  style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>11. Photo ID Card/ Certificate
  having address issued by Central/State Govt.</p>
  </td>
 </tr>
</table>

<p class=MsoNormal><span style=''font-size:16.0pt;line-height:107%;font-family:
"Arial",sans-serif;color:#1D1C1D;background:white''>&nbsp;</span></p>

<p class=MsoNormal><span style=''font-size:16.0pt;line-height:107%;font-family:
"Arial",sans-serif;color:#1D1C1D;background:white''>&nbsp;</span></p>

<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0
 style=''border-collapse:collapse;border:none''>
 <tr>
  <td width=623 colspan=2 valign=top style=''width:467.5pt;border:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoNormal align=center style=''margin-bottom:0in;text-align:center;
  line-height:normal''><b><span style=''font-size:14.0pt;font-family:"Arial",sans-serif;
  color:#1D1C1D;background:aqua''>POR (Proof of Relationship)</span></b></p>
  <p class=MsoNormal align=center style=''margin-bottom:0in;text-align:center;
  line-height:normal''><span style=''background:white''>&nbsp;</span></p>
  </td>
 </tr>
 <tr>
  <td width=274 valign=top style=''width:205.3pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoNormal align=center style=''margin-bottom:0in;text-align:center;
  line-height:normal''><b><span style=''font-size:11.5pt;font-family:"Arial",sans-serif;
  color:#1D1C1D;background:white''>The POR documents specification:</span></b></p>
  </td>
  <td width=350 valign=top style=''width:262.2pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraphCxSpFirst style=''margin-bottom:0in;text-indent:-.25in;
  line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>1. It should contain the Introducer’s Full Name and a clear Photo</p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:
  -.25in;line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>2. It should contain the applicant’s Full Name and a clear Photo</p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:
  -.25in;line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>3. The relationship between the Introducer and resident should be clearly
  mentioned</p>
  <p class=MsoListParagraphCxSpLast style=''margin-bottom:0in;text-indent:-.25in;
  line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>4. All the produced documents should be valid for at least the next 6
  months</p>
  </td>
 </tr>
 <tr>
  <td width=274 valign=top style=''width:205.3pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoNormal align=center style=''margin-bottom:0in;text-align:center;
  line-height:normal''><b><span style=''font-size:11.5pt;font-family:"Arial",sans-serif;
  color:#1D1C1D;background:white''>Acceptable document:</span></b></p>
  <p class=MsoNormal align=center style=''margin-bottom:0in;text-align:center;
  line-height:normal''><span style=''font-size:16.0pt;font-family:"Arial",sans-serif;
  color:#1D1C1D;background:white''>&nbsp;</span></p>
  </td>
  <td width=350 valign=top style=''width:262.2pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraphCxSpFirst style=''margin-bottom:0in;text-indent:-.25in;
  line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>1. Passport of Spouse/ Passport of Parents (in case of Minor) </p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:
  -.25in;line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>2. Ration card/PDS Card</p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:
  -.25in;line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>3. Medi-Claim Card with Photo issued by Centre/ State Govts./ PSUs </p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:
  -.25in;line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>4. Pension Card</p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:
  -.25in;line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>5. Birth Certificate issued by Registrar of Birth, Municipal Corporation,
  and other notified local government bodies </p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:
  -.25in;line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>6. Any other Central/ State government-issued family entitlement document
  </p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:
  -.25in;line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>7. Marriage Certificate issued by the government </p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:
  -.25in;line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>8. Photo ID card issued by Central/ State Govt. like ARMY canteen card
  etc. </p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:
  -.25in;line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>9. Discharge card/ slip issued by Government/ Private Hospitals for birth
  of a child (only for child aged between 0-5 years) </p>
  <p class=MsoListParagraphCxSpLast style=''margin-bottom:0in;text-indent:-.25in;
  line-height:normal''><span style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  </span>10. Self-declaration from the Head of Family (HoF) certifying the
  relationship with the resident residing at the same address as HoF<span
  style=''font-size:11.5pt;font-family:"Arial",sans-serif;color:#1D1C1D;
  background:white''> </span></p>
  </td>
 </tr>
</table>

<p class=MsoNormal><span style=''font-size:16.0pt;line-height:107%;font-family:
"Arial",sans-serif;color:#1D1C1D;background:white''>&nbsp;</span></p>

<p class=MsoNormal><span style=''font-size:16.0pt;line-height:107%;font-family:
"Arial",sans-serif;color:#1D1C1D;background:white''>&nbsp;</span></p>

<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0
 style=''border-collapse:collapse;border:none''>
 <tr>
  <td width=623 colspan=2 valign=top style=''width:467.5pt;border:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoNormal align=center style=''margin-bottom:0in;text-align:center;
  line-height:normal''><b><span style=''font-size:14.0pt;font-family:"Arial",sans-serif;
  color:#1D1C1D;background:aqua''>DOB (Date of Birth)</span></b></p>
  <p class=MsoNormal align=center style=''margin-bottom:0in;text-align:center;
  line-height:normal''><span style=''background:white''>&nbsp;</span></p>
  </td>
 </tr>
 <tr style=''height:34.7pt''>
  <td width=217 valign=top style=''width:162.8pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt;height:34.7pt''>
  <p class=MsoNormal align=center style=''margin-bottom:0in;text-align:center;
  line-height:normal''><b><span style=''font-size:11.5pt;font-family:"Arial",sans-serif;
  color:#1D1C1D;background:white''>The DOB documents specification:</span></b></p>
  </td>
  <td width=406 valign=top style=''width:304.7pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt;height:34.7pt''>
  <p class=MsoListParagraphCxSpFirst style=''margin-bottom:0in;text-indent:-.25in;line-height:normal''><span
  style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>1. It
  should contain Full Name, a clear Photo and DOB</p>
  <p class=MsoListParagraphCxSpLast style=''margin-bottom:0in;text-indent:-.25in;line-height:normal''><span
  style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>2. It
  should be valid for at least the next 6 months</p>
  </td>
 </tr>
 <tr>
  <td width=217 valign=top style=''width:162.8pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoNormal align=center style=''margin-bottom:0in;text-align:center;
  line-height:normal''><b><span style=''font-size:11.5pt;font-family:"Arial",sans-serif;
  color:#1D1C1D;background:white''>Acceptable document:</span></b></p>
  <p class=MsoNormal align=center style=''margin-bottom:0in;text-align:center;
  line-height:normal''><span style=''font-size:16.0pt;font-family:"Arial",sans-serif;
  color:#1D1C1D;background:white''>&nbsp;</span></p>
  </td>
  <td width=406 valign=top style=''width:304.7pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraphCxSpFirst style=''margin-bottom:0in;text-indent:-.25in;line-height:normal''><span
  style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>1. Birth Certificate </p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:-.25in;line-height:normal''><span
  style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>2. Passport</p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:-.25in;line-height:normal''><span
  style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>3. PAN Card/e-PAN</p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:-.25in;line-height:normal''><span
  style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>4. Service photo
  identity card issued by Central Govt./State Govt./UT Govt./PSU/Banks </p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:-.25in;line-height:normal''><span
  style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>5. Photo ID card having
  Date of Birth, issued by Recognized Educational Institution</p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:-.25in;line-height:normal''><span
  style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>6. Valid School
  Identity card/Identity Card issued by recognized educational institutions</p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:-.25in;line-height:normal''><span
  style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>7. Gas Connection Bill</p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:-.25in;line-height:normal''><span
  style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>8. Marriage Certificate
  issued by the Government containing Name and address</p>
  <p class=MsoListParagraphCxSpMiddle style=''margin-bottom:0in;text-indent:-.25in;line-height:normal''><span
  style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>9. Registered Sale/
  Rent Agreement</p>
  <p class=MsoListParagraphCxSpLast style=''margin-bottom:0in;text-indent:-.25in;line-height:normal''><span
  style=''font:7.0pt "Times New Roman"''>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span>10. Telephone Landline
  Bill/ Phone (Postpaid Mobile) Bill/ Broadband Bill</p>
  </td>
 </tr>
</table>

<p class=MsoNormal style=''margin-bottom:0in''><span style=''font-size:11.5pt;
line-height:107%;font-family:"Arial",sans-serif;color:#1D1C1D;background:white''>&nbsp;</span></p>

<p class=MsoNormal style=''margin-bottom:0in''><span style=''font-size:11.5pt;
line-height:107%;font-family:"Arial",sans-serif;color:#1D1C1D;background:white''>&nbsp;</span></p>

<p class=MsoNormal style=''margin-bottom:0in''><span style=''font-size:11.5pt;
line-height:107%;font-family:"Arial",sans-serif;color:#1D1C1D;background:white''>&nbsp;</span></p>

</div>

</body>

</html>', module_id='10006', module_name='Resident Services', template_typ_code='supporting-docs-list', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1500' AND lang_code='eng';
UPDATE master.template
SET "name"='Acknowledgement for manage my vid', descr='Acknowledgement for manage my vid', file_format_code='html', model='velocity', file_txt='<html>

<head>
    
</head>

<body lang=EN-US link=blue vlink="#954F72" style=''tab-interval:.5in;word-wrap:break-word''>

<div class=WordSection1>

<div>

<p class=MsoNormal align=center style=''text-align:center''><span lang=EN-IN><img
width=200 height=100 id="Picture 2"
src="https://mosip.io/images/logo.png" alt="MOSIP"></span></p>
<p class=MsoNormal align=center style=''text-align:center''><span lang=EN-IN><img
width=50 height=50 id="partnerlogo"
src="$partnerLogo" alt="partner logo"></span></p>

<table class=MsoNormalTable align=center border=0 cellspacing=0 cellpadding=0 width=586
 style=''width:439.4pt;border-collapse:collapse;mso-yfti-tbllook:
 1184;mso-padding-alt:0in 0in 0in 0in''>
 <tr style=''mso-yfti-irow:0;mso-yfti-firstrow:yes''>
  <td width=586 colspan=2 valign=top style=''width:439.4pt;border:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph align=center style=''margin:0in;text-align:center''><b><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Request details</span></b></p>
  </td>
 </tr>
 
 <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event request timestamp: </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$timestamp</span></p></td>
  </tr>
  
  <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event Id:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventId</span></p></td>
  </tr>
  
  <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Authentication mode:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$authenticationMode</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event Type:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventType</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event status:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventStatus</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Summary  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$summary</span></p></td>
  </tr>
</table>

<p class=MsoListParagraph align=center style=''text-align:center''><span
lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
mso-ansi-language:EN-IN''>&nbsp;</span></p>

<p class=MsoListParagraph align=center style=''text-align:center''><span
lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
mso-ansi-language:EN-IN''>To view the status of your event, return to&nbsp;</span><span
lang=EN-IN style=''mso-ansi-language:EN-IN''><a
href=""><span
style=''font-family:"Verdana",sans-serif;color:#004B91;background:white''><a href="$trackServiceRequestLink">Event
Summary</a></span></a></span><span lang=EN-IN style=''font-family:"Verdana",sans-serif;
color:black;background:white;mso-ansi-language:EN-IN''>.</span></p>

<p class=MsoNormal style=''mso-margin-top-alt:auto;mso-margin-bottom-alt:auto''><span
lang=EN-IN style=''mso-ansi-language:EN-IN''>&nbsp;</span></p>

</div>

</div>

</body>

</html>', module_id='10006', module_name='Resident Services', template_typ_code='acknowledgement-manage-my-vid', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1501' AND lang_code='eng';
UPDATE master.template
SET "name"='Acknowledgement for order a physical card', descr='Acknowledgement for order a physical card', file_format_code='html', model='velocity', file_txt='<html>

<head>

</head>

<body lang=EN-US link=blue vlink="#954F72" style=''tab-interval:.5in;word-wrap:break-word''>

<div class=WordSection1>

<div>

<p class=MsoNormal align=center style=''text-align:center''><span lang=EN-IN><img
width=200 height=100 id="Picture 2"
src="https://mosip.io/images/logo.png" alt="MOSIP"></span></p>

<p class=MsoNormal align=center style=''text-align:center''><span lang=EN-IN><img
width=50 height=50 id="partnerlogo"
src="$partnerLogo" alt="partner logo"></span></p>

<table class=MsoNormalTable border=0 cellspacing=0 cellpadding=0 width=586
 style=''width:439.4pt;margin-left:21.05pt;border-collapse:collapse;mso-yfti-tbllook:
 1184;mso-padding-alt:0in 0in 0in 0in''>
 <tr style=''mso-yfti-irow:0;mso-yfti-firstrow:yes''>
  <td width=586 colspan=2 valign=top style=''width:439.4pt;border:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph align=center style=''margin:0in;text-align:center''><b><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Request details</span></b></p>
  </td>
 </tr>
 <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event request timestamp: </span></p>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event Id: </span></p>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Authentication mode:</span></p>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event Type: </span></p>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Purpose:</span></p>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event status: </span></p>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Summary:</span></p>
  </td>
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$timestamp</span></p>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventId</span></span></p>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$authenticationMode</span></span></p>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventType</span></span></p>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>$purpose</span></p>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>$eventStatus</span></span></p>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>$summary</span></p>
  <p class=MsoListParagraph style=''margin:0in''><b><span lang=EN-IN
  style=''mso-ansi-language:EN-IN''>&nbsp;</span></b></p>
  </td>
 </tr>
 <tr style=''mso-yfti-irow:2''>
  <td width=586 colspan=2 valign=top style=''width:439.4pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph align=center style=''margin:0in;text-align:center''><b><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Card details</span></b></p>
  </td>
 </tr>

 <tr style=''mso-yfti-irow:3''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>Attributes added to the card:</span></p>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>&nbsp;</span></p>
  <p class=MsoListParagraph style=''margin:0in''><b><span lang=EN-IN
  style=''mso-ansi-language:EN-IN''>&nbsp;</span></b></p>
  </td>
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$attributeList</span></span></p>
  <p class=MsoListParagraph style=''margin:0in''><b><span lang=EN-IN
  style=''mso-ansi-language:EN-IN''>&nbsp;</span></b></p>
  </td>
 </tr>
 <tr style=''mso-yfti-irow:4''>
  <td width=586 colspan=2 valign=top style=''width:439.4pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph align=center style=''margin:0in;text-align:center''><b><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Delivery details</span></b></p>
  </td>
 </tr>

 <tr style=''mso-yfti-irow:5''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>Delivery Address:</span></p>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>&nbsp;</span></p>
  <p class=MsoListParagraph style=''margin:0in''><b><span lang=EN-IN
  style=''mso-ansi-language:EN-IN''>&nbsp;</span></b></p>
     <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>Delivery options:</span></p>
     <p class=MsoListParagraph style=''margin:0in''><b><span lang=EN-IN
  style=''mso-ansi-language:EN-IN''>&nbsp;</span></b></p>
     <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>Other details from the vendor''s end:</span></p>
  </td>
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$address</span></span></p>
  <p class=MsoListParagraph style=''margin:0in''><b><span lang=EN-IN
  style=''mso-ansi-language:EN-IN''>&nbsp;</span></b></p>
 <p class=MsoListParagraph style=''margin:0in''><b><span lang=EN-IN
  style=''mso-ansi-language:EN-IN''>&nbsp;</span></b></p>
<p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$deliveryOptions</span></span></p>
 <p class=MsoListParagraph style=''margin:0in''><b><span lang=EN-IN
  style=''mso-ansi-language:EN-IN''>&nbsp;</span></b></p>
<p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$otherDetails</span></span></p>
  </td>
 </tr>
 <tr style=''mso-yfti-irow:6''>
  <td width=586 colspan=2 valign=top style=''width:439.4pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph align=center style=''margin:0in;text-align:center''><b><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Payment details</span></b></p>
  </td>
 </tr>

 <tr style=''mso-yfti-irow:7''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>Payment status:</span></p>
     <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>Payment Method:</span></p>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>&nbsp;</span></p>
     <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$paymentMode</span></p>
  <p class=MsoListParagraph style=''margin:0in''><b><span lang=EN-IN
  style=''mso-ansi-language:EN-IN''>&nbsp;</span></b></p>
  </td>
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$paymentStatus</span></span></p>
 <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$breakDownOfCost</span></span></p>
 <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$totalCost</span></span></p>
  <p class=MsoListParagraph style=''margin:0in''><b><span lang=EN-IN
  style=''mso-ansi-language:EN-IN''>&nbsp;</span></b></p>
  </td>
 </tr>
 <tr style=''mso-yfti-irow:8''>
  <td width=586 colspan=2 valign=top style=''width:439.4pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph align=center style=''margin:0in;text-align:center''><b><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Partner Details</span></b></p>
  </td>
 </tr>
 <tr style=''mso-yfti-irow:5;mso-yfti-lastrow:yes''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>Partner name:</span></p>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>Tracking Id:</span></p>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>Tracking link:</span></p>
  </td>
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$partnerName</span><o:p></o:p></span></p>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$trackingId</span><o:p></o:p></span></p>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$orderTrackingLink</span><o:p></o:p></span></p>
  </td>
 </tr>
</table>

<p class=MsoListParagraph align=center style=''text-align:center''><span
lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
mso-ansi-language:EN-IN''>&nbsp;</span></p>

<p class=MsoListParagraph align=center style=''text-align:center''><span
lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
mso-ansi-language:EN-IN''>To view the status of your order, return to&nbsp;</span><span
lang=EN-IN style=''mso-ansi-language:EN-IN''><a
href="$trackServiceRequestLink"><span
style=''font-family:"Verdana",sans-serif;color:#004B91;background:white''>Event
Summary</span></a></span><span lang=EN-IN style=''font-family:"Verdana",sans-serif;
color:black;background:white;mso-ansi-language:EN-IN''>.</span></p>

<p class=MsoNormal style=''mso-margin-top-alt:auto;mso-margin-bottom-alt:auto''><span
lang=EN-IN style=''mso-ansi-language:EN-IN''>&nbsp;</span></p>

</div>

</div>

</body>

</html>', module_id='10006', module_name='Resident Services', template_typ_code='acknowledgement-order-a-physical-card', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1502' AND lang_code='eng';
UPDATE master.template
SET "name"='Acknowledgement for Download a personalized card', descr='Acknowledgement for Download a personalized card', file_format_code='html', model='velocity', file_txt='<html>

<head>

</head>

<body lang=EN-US link=blue vlink="#954F72" style=''tab-interval:.5in;word-wrap:break-word''>

<div class=WordSection1>

<div>

<p class=MsoNormal align=center style=''text-align:center''><span lang=EN-IN><img
width=200 height=100 id="Picture 2"
src="https://mosip.io/images/logo.png" alt="MOSIP"></span></p>

<table class=MsoNormalTable border=0 cellspacing=0 cellpadding=0 width=586
 style=''width:439.4pt;margin-left:61.05pt;border-collapse:collapse;mso-yfti-tbllook:
 1184;mso-padding-alt:0in 0in 0in 0in''>
 <tr style=''mso-yfti-irow:0;mso-yfti-firstrow:yes''>
  <td width=586 colspan=2 valign=top style=''width:439.4pt;border:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph align=center style=''margin:0in;text-align:center''><b><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Request details</span></b></p>
  </td>
 </tr>
 <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event request timestamp: </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$timestamp</span></p></td>
  </tr>
  
  <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event Id:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventId</span></p></td>
  </tr>
  
  <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Authentication mode:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$authenticationMode</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event Type:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventType</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event status:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventStatus</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Summary  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$summary</span></p></td>
  </tr>
 <tr style=''mso-yfti-irow:2''>
  <td width=586 colspan=2 valign=top style=''width:439.4pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph align=center style=''margin:0in;text-align:center''><b><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Card details</span></b></p>
  </td>
 </tr>

 <tr style=''mso-yfti-irow:3''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>Attributes added to the card:</span></p>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>&nbsp;</span></p>
  <p class=MsoListParagraph style=''margin:0in''><b><span lang=EN-IN
  style=''mso-ansi-language:EN-IN''>&nbsp;</span></b></p>
  </td>
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$attributeList</span></span></p>
  <p class=MsoListParagraph style=''margin:0in''><b><span lang=EN-IN
  style=''mso-ansi-language:EN-IN''>&nbsp;</span></b></p>
  </td>
 </tr>
 
 
</table>

<p class=MsoListParagraph align=center style=''text-align:center''><span
lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
mso-ansi-language:EN-IN''>&nbsp;</span></p>

<p class=MsoListParagraph align=center style=''text-align:center''><span
lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
mso-ansi-language:EN-IN''>To view the status of your event, return to&nbsp;</span><span
lang=EN-IN style=''mso-ansi-language:EN-IN''><a
href=""><span
style=''font-family:"Verdana",sans-serif;color:#004B91;background:white''><a href="$trackServiceRequestLink">Event
Summary</a></span></a></span><span lang=EN-IN style=''font-family:"Verdana",sans-serif;
color:black;background:white;mso-ansi-language:EN-IN''>.</span></p>

<p class=MsoNormal style=''mso-margin-top-alt:auto;mso-margin-bottom-alt:auto''><span
lang=EN-IN style=''mso-ansi-language:EN-IN''>&nbsp;</span></p>

</div>

</div>

</body>

</html>', module_id='10006', module_name='Resident Services', template_typ_code='acknowledgement-download-a-personalized-card', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1503' AND lang_code='eng';
UPDATE master.template
SET "name"='Acknowledgement for Update demographic data', descr='Acknowledgement for Update demographic data', file_format_code='html', model='velocity', file_txt='<html>

<head>

</head>

<body lang=EN-US link=blue vlink="#954F72" style=''tab-interval:.5in;word-wrap:break-word''>

<div class=WordSection1>

<div>

<p class=MsoNormal align=center style=''text-align:center''><span lang=EN-IN><img
width=200 height=100 id="Picture 2"
src="https://mosip.io/images/logo.png" alt="MOSIP"></span></p>

<table class=MsoNormalTable border=0 cellspacing=0 cellpadding=0 width=586
 style=''width:439.4pt;margin-left:61.05pt;border-collapse:collapse;mso-yfti-tbllook:
 1184;mso-padding-alt:0in 0in 0in 0in''>
 <tr style=''mso-yfti-irow:0;mso-yfti-firstrow:yes''>
  <td width=586 colspan=2 valign=top style=''width:439.4pt;border:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph align=center style=''margin:0in;text-align:center''><b><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Request details</span></b></p>
  </td>
 </tr>
 <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event request timestamp: </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$timestamp</span></p></td>
  </tr>
  
  <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event Id:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventId</span></p></td>
  </tr>
  
  <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Authentication mode:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$authenticationMode</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event Type:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventType</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event status:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventStatus</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Summary  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$summary</span></p></td>
  </tr>
 <tr style=''mso-yfti-irow:2''>
  <td width=586 colspan=2 valign=top style=''width:439.4pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph align=center style=''margin:0in;text-align:center''><b><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Update details</span></b></p>
  </td>
 </tr>

 <tr style=''mso-yfti-irow:3''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>Attributes added to the card:</span></p>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>&nbsp;</span></p>
  <p class=MsoListParagraph style=''margin:0in''><b><span lang=EN-IN
  style=''mso-ansi-language:EN-IN''>&nbsp;</span></b></p>
  </td>
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$attributeList</span></span></p>
  <p class=MsoListParagraph style=''margin:0in''><b><span lang=EN-IN
  style=''mso-ansi-language:EN-IN''>&nbsp;</span></b></p>
  </td>
 </tr>
 
 
</table>

<p class=MsoListParagraph align=center style=''text-align:center''><span
lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
mso-ansi-language:EN-IN''>&nbsp;</span></p>

<p class=MsoListParagraph align=center style=''text-align:center''><span
lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
mso-ansi-language:EN-IN''>To view the status of your event, return to&nbsp;</span><span
lang=EN-IN style=''mso-ansi-language:EN-IN''><a
href=""><span
style=''font-family:"Verdana",sans-serif;color:#004B91;background:white''><a href="$trackServiceRequestLink">Event
Summary</a></span></a></span><span lang=EN-IN style=''font-family:"Verdana",sans-serif;
color:black;background:white;mso-ansi-language:EN-IN''>.</span></p>

<p class=MsoNormal style=''mso-margin-top-alt:auto;mso-margin-bottom-alt:auto''><span
lang=EN-IN style=''mso-ansi-language:EN-IN''>&nbsp;</span></p>

</div>

</div>

</body>

</html>', module_id='10006', module_name='Resident Services', template_typ_code='acknowledgement-update-demographic-data', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1504' AND lang_code='eng';
UPDATE master.template
SET "name"='Acknowledgement for verify email id or phone number', descr='Acknowledgement for verify email id or phone number', file_format_code='html', model='velocity', file_txt='<html>

<head>

</head>

<body lang=EN-US link=blue vlink="#954F72" style=''tab-interval:.5in;word-wrap:break-word''>

<div class=WordSection1>

<div>

<p class=MsoNormal align=center style=''text-align:center''><span lang=EN-IN><img
width=200 height=100 id="Picture 2"
src="https://mosip.io/images/logo.png" alt="MOSIP"></span></p>

<table class=MsoNormalTable border=0 cellspacing=0 cellpadding=0 width=586
 style=''width:439.4pt;margin-left:61.05pt;border-collapse:collapse;mso-yfti-tbllook:
 1184;mso-padding-alt:0in 0in 0in 0in''>
 <tr style=''mso-yfti-irow:0;mso-yfti-firstrow:yes''>
  <td width=586 colspan=2 valign=top style=''width:439.4pt;border:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph align=center style=''margin:0in;text-align:center''><b><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Request details</span></b></p>
  </td>
 </tr>
 <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event request timestamp: </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$timestamp</span></p></td>
  </tr>
  
  <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event Id:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventId</span></p></td>
  </tr>
  
  <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Authentication mode:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$authenticationMode</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event Type:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventType</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event status:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventStatus</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Summary  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$summary</span></p></td>
  </tr>
 <tr style=''mso-yfti-irow:2''>
  <td width=586 colspan=2 valign=top style=''width:439.4pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph align=center style=''margin:0in;text-align:center''><b><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Verification details</span></b></p>
  </td>
 </tr>

 <tr style=''mso-yfti-irow:3''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>Channel that was verified:</span></p>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>&nbsp;</span></p>
  <p class=MsoListParagraph style=''margin:0in''><b><span lang=EN-IN
  style=''mso-ansi-language:EN-IN''>&nbsp;</span></b></p>
  </td>
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$channel</span></span></p>
  <p class=MsoListParagraph style=''margin:0in''><b><span lang=EN-IN
  style=''mso-ansi-language:EN-IN''>&nbsp;</span></b></p>
  </td>
 </tr>
 
 
</table>
<p class=MsoListParagraph align=center style=''text-align:center''><span
lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
mso-ansi-language:EN-IN''>&nbsp;</span></p>

<p class=MsoListParagraph align=center style=''text-align:center''><span
lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
mso-ansi-language:EN-IN''>To view the status of your event, return to&nbsp;</span><span
lang=EN-IN style=''mso-ansi-language:EN-IN''><a
href=""><span
style=''font-family:"Verdana",sans-serif;color:#004B91;background:white''><a href="$trackServiceRequestLink">Event
Summary</a></span></a></span><span lang=EN-IN style=''font-family:"Verdana",sans-serif;
color:black;background:white;mso-ansi-language:EN-IN''>.</span></p>

<p class=MsoNormal style=''mso-margin-top-alt:auto;mso-margin-bottom-alt:auto''><span
lang=EN-IN style=''mso-ansi-language:EN-IN''>&nbsp;</span></p>

</div>

</div>

</body>

</html>', module_id='10006', module_name='Resident Services', template_typ_code='acknowledgement-verify-email-id-or-phone-number', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1505' AND lang_code='eng';
UPDATE master.template
SET "name"='Acknowledgement for Secure my Id', descr='Acknowledgement for Secure my Id', file_format_code='html', model='velocity', file_txt='<html>

<head>

</head>

<body lang=EN-US link=blue vlink="#954F72" style=''tab-interval:.5in;word-wrap:break-word''>

    <div class=WordSection1>

        <div>

            <p class=MsoNormal align=center style=''text-align:center''><span lang=EN-IN><img width=200 height=100
                        id="Picture 2" src="https://mosip.io/images/logo.png" alt="MOSIP"></span></p>

            <table class=MsoNormalTable border=0 cellspacing=0 cellpadding=0 width=586 style=''width:439.4pt;margin-left:51.05pt;border-collapse:collapse;mso-yfti-tbllook:
 1184;mso-padding-alt:0in 0in 0in 0in''>
                <tr style=''mso-yfti-irow:0;mso-yfti-firstrow:yes''>
                    <td width=586 colspan=2 valign=top style=''width:439.4pt;border:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
                        <p class=MsoListParagraph align=center style=''margin:0in;text-align:center''><b><span lang=EN-IN
                                    style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Request details</span></b></p>
                    </td>
                </tr>
                <tr style=''mso-yfti-irow:1''>
                    <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
                        <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event request timestamp: </span></p>
                    </td>

                    <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
                        <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$timestamp</span></p>
                    </td>
                </tr>

                <tr style=''mso-yfti-irow:1''>
                    <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
                        <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event Id: </span></p>
                    </td>

                    <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
                        <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventId</span></p>
                    </td>
                </tr>

                <tr style=''mso-yfti-irow:1''>
                    <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
                        <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Authentication mode: </span></p>
                    </td>

                    <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
                        <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$authenticationMode</span></p>
                    </td>
                </tr>
                <tr style=''mso-yfti-irow:1''>
                    <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
                        <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event Type: </span></p>
                    </td>

                    <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
                        <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventType</span></p>
                    </td>
                </tr>
                <tr style=''mso-yfti-irow:1''>
                    <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
                        <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event status: </span></p>
                    </td>

                    <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
                        <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventStatus</span></p>
                    </td>
                </tr>
                <tr style=''mso-yfti-irow:1''>
                    <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
                        <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Summary </span></p>
                    </td>

                    <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
                        <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$summary</span></p>
                    </td>
                </tr>


            </table>

            <p class=MsoListParagraph align=center style=''text-align:center''><span lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
mso-ansi-language:EN-IN''>&nbsp;</span></p>

            <p class=MsoListParagraph align=center style=''text-align:center''><span lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
mso-ansi-language:EN-IN''>To view the status of your event, return to&nbsp;</span><span lang=EN-IN
                    style=''mso-ansi-language:EN-IN''><a href=""><span
                            style=''font-family:"Verdana",sans-serif;color:#004B91;background:white''><a
                                href="$trackServiceRequestLink">Event
                                Summary</a></span></a></span><span lang=EN-IN style=''font-family:"Verdana",sans-serif;
color:black;background:white;mso-ansi-language:EN-IN''>.</span></p>


            <p class=MsoNormal style=''mso-margin-top-alt:auto;mso-margin-bottom-alt:auto''><span lang=EN-IN
                    style=''mso-ansi-language:EN-IN''>&nbsp;</span></p>

        </div>

    </div>

</body>

</html>', module_id='10006', module_name='Resident Services', template_typ_code='acknowledgement-secure-my-id', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1506' AND lang_code='eng';
UPDATE master.template
SET "name"='List of registration centers', descr='List of registration centers', file_format_code='html', model='velocity', file_txt='<html>

<head>
	<meta charset=UTF-8>
	<meta name=viewport content=width=device-width, initial-scale=1.0>
	<meta http-equiv=X-UA-Compatible content=ie=edge>
	<title>registration centers</title>
</head>

<body lang=EN-US style=''word-wrap: break-word''>

	<p class=MsoNormal align=center style=''text-align:center''><span lang=EN-IN><img width=200 height=100 id=Picture 2
				src=https://mosip.io/images/logo.png alt=MOSIP></span></p>

	<p class=MsoNormal align=center style=''text-align: center''>
		<b><span
				style=''font-size: 16.0pt; line-height: 106%; font-family: Arial, sans-serif; color:#1D1C1D; background: yellow''>Registration
				centers</span></b>
	</p>

	<p class=MsoNormal align=center style=''text-align: center''>&nbsp;</p>

	<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0 width=737
		style=''width:90%;margin:auto;border-collapse: collapse;''>
		<tr>
			<td width=104 valign=top
				style=''width: 78.0pt; border: solid windowtext 1.0pt; padding: 0in 5.4pt 0in 5.4pt''>
				<p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: center; line-height: normal''>
					<b>Serial number</b>
				</p>
			</td>
			<td width=104 valign=top
				style=''width: 78.0pt; border: solid windowtext 1.0pt; border-left: none; padding: 0in 5.4pt 0in 5.4pt''>
				<p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: center; line-height: normal''>
					<b>Registration center name</b>
				</p>
			</td>
			<td width=104 valign=top
				style=''width: 78.0pt; border: solid windowtext 1.0pt; border-left: none; padding: 0in 5.4pt 0in 5.4pt''>
				<p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: center; line-height: normal''>
					<b>Registration center address</b>
				</p>
			</td>
			<td width=104 valign=top
				style=''width: 78.0pt; border: solid windowtext 1.0pt; border-left: none; padding: 0in 5.4pt 0in 5.4pt''>
				<p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: center; line-height: normal''>
					<b>Contact number</b>
				</p>
			</td>
			<td width=104 valign=top
				style=''width: 78.0pt; border: solid windowtext 1.0pt; border-left: none; padding: 0in 5.4pt 0in 5.4pt''>
				<p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: center; line-height: normal''>
					<b>Days of week | Timings</b>
				</p>
			</td>
		</tr>
		#foreach($dto in $regCenterIntialList)
		<tr>
			<td width=104 valign=top
				style=''width: 78.0pt; border: solid windowtext 1.0pt; padding: 0in 5.4pt 0in 5.4pt''>
				<p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: center; line-height: normal''>
					<span>$dto.serialNumber</span>
				</p>
			</td>
			<td width=104 valign=top
				style=''width: 78.0pt; border: solid windowtext 1.0pt; border-left: none; padding: 0in 5.4pt 0in 5.4pt''>
				<p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: center; line-height: normal''>
					<span>$dto.name</span>
				</p>
			</td>
			<td width=104 valign=top
				style=''width: 78.0pt; border: solid windowtext 1.0pt; border-left: none; padding: 0in 5.4pt 0in 5.4pt''>
				<p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: center; line-height: normal''>
					<span>$dto.fullAddress</span>
				</p>
			</td>
			<td width=104 valign=top
				style=''width: 78.0pt; border: solid windowtext 1.0pt; border-left: none; padding: 0in 5.4pt 0in 5.4pt''>
				<p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: center; line-height: normal''>
					<span>$dto.contactPhone</span>
				</p>
			</td>
			<td width=104 valign=top
				style=''width: 78.0pt; border: solid windowtext 1.0pt; border-left: none; padding: 0in 5.4pt 0in 5.4pt''>
				<p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: center; line-height: normal''>
					<span>$dto.workingHours</span>
				</p>
			</td>
		</tr>
		#end
	</table>
	<br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
</body>

</html>', module_id='10006', module_name='Resident Services', template_typ_code='registration-centers-list', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1507' AND lang_code='eng';
UPDATE master.template
SET "name"='Receiving OTP Mail Subject', descr='Receiving OTP mail Subject', file_format_code='txt', model='velocity', file_txt='OTP | $eventDetails', module_id='10006', module_name='Resident Services', template_typ_code='receive-otp-mail-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1508' AND lang_code='tam';
UPDATE master.template
SET "name"='Receiving OTP Mail Subject', descr='Receiving OTP mail Subject', file_format_code='txt', model='velocity', file_txt='ओटीपी | $eventDetails', module_id='10006', module_name='Resident Services', template_typ_code='receive-otp-mail-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1508' AND lang_code='hin';
UPDATE master.template
SET "name"='Receiving OTP Mail Subject', descr='Receiving OTP mail Subject', file_format_code='txt', model='velocity', file_txt='OTP | $eventDetails', module_id='10006', module_name='Resident Services', template_typ_code='receive-otp-mail-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1508' AND lang_code='kan';
UPDATE master.template
SET "name"='Receiving OTP Mail Subject', descr='Receiving OTP mail Subject', file_format_code='txt', model='velocity', file_txt='OTP | $eventDetails', module_id='10006', module_name='Resident Services', template_typ_code='receive-otp-mail-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1508' AND lang_code='fra';
UPDATE master.template
SET "name"='Receiving OTP Mail Subject', descr='Receiving OTP mail Subject', file_format_code='txt', model='velocity', file_txt='OTP | $eventDetails', module_id='10006', module_name='Resident Services', template_typ_code='receive-otp-mail-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1508' AND lang_code='eng';
UPDATE master.template
SET "name"='Receiving OTP Mail Subject', descr='Receiving OTP mail Subject', file_format_code='txt', model='velocity', file_txt='OTP | $eventDetails', module_id='10006', module_name='Resident Services', template_typ_code='receive-otp-mail-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1508' AND lang_code='ara';
UPDATE master.template
SET "name"='Receiving OTP Mail Content', descr='Receiving OTP Mail Content', file_format_code='txt', model='velocity', file_txt='Cher $name, <br>
Utilisez OTP $otp pour mettre à jour vos données.', module_id='10006', module_name='Resident Services', template_typ_code='receive-otp-mail-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1509' AND lang_code='fra';
UPDATE master.template
SET "name"='Receiving OTP Mail Content', descr='Receiving OTP Mail Content', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Use OTP $otp to update your data.', module_id='10006', module_name='Resident Services', template_typ_code='receive-otp-mail-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1509' AND lang_code='eng';
UPDATE master.template
SET "name"='Receiving OTP Mail Content', descr='Receiving OTP Mail Content', file_format_code='txt', model='velocity', file_txt='प्रिय $name, <br>
अपना डेटा अपडेट करने के लिए OTP $otp का उपयोग करें।', module_id='10006', module_name='Resident Services', template_typ_code='receive-otp-mail-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1509' AND lang_code='hin';
UPDATE master.template
SET "name"='Receiving OTP Mail Content', descr='Receiving OTP Mail Content', file_format_code='txt', model='velocity', file_txt='ಆತ್ಮೀಯ $name, <br>
ನಿಮ್ಮ ಡೇಟಾವನ್ನು ನವೀಕರಿಸಲು OTP $otp ಬಳಸಿ.', module_id='10006', module_name='Resident Services', template_typ_code='receive-otp-mail-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1509' AND lang_code='kan';
UPDATE master.template
SET "name"='Receiving OTP Mail Content', descr='Receiving OTP Mail Content', file_format_code='txt', model='velocity', file_txt='அன்புள்ள $name, <br>
உங்கள் தரவைப் புதுப்பிக்க OTP $otp ஐப் பயன்படுத்தவும்.', module_id='10006', module_name='Resident Services', template_typ_code='receive-otp-mail-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1509' AND lang_code='tam';
UPDATE master.template
SET "name"='Receiving OTP Mail Content', descr='Receiving OTP Mail Content', file_format_code='txt', model='velocity', file_txt='عزيزي $name ، <br>
استخدم $OTP otp لتحديث بياناتك.', module_id='10006', module_name='Resident Services', template_typ_code='receive-otp-mail-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1509' AND lang_code='ara';
UPDATE master.template
SET "name"='Receive OTP', descr='Receive OTP', file_format_code='txt', model='velocity', file_txt='$eventDetails: Use OTP $otp to update your data.', module_id='10006', module_name='Resident Services', template_typ_code='receive-otp', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1512' AND lang_code='eng';
UPDATE master.template
SET "name"='Receive OTP', descr='Receive OTP', file_format_code='txt', model='velocity', file_txt='$eventDetails : utilisez OTP $otp pour mettre à jour vos données.', module_id='10006', module_name='Resident Services', template_typ_code='receive-otp', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1512' AND lang_code='fra';
UPDATE master.template
SET "name"='Receive OTP', descr='Receive OTP', file_format_code='txt', model='velocity', file_txt='$eventDetails: உங்கள் தரவைப் புதுப்பிக்க OTP $otp ஐப் பயன்படுத்தவும்.', module_id='10006', module_name='Resident Services', template_typ_code='receive-otp', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1512' AND lang_code='tam';
UPDATE master.template
SET "name"='Receive OTP', descr='Receive OTP', file_format_code='txt', model='velocity', file_txt='$eventDetails : استخدم OTP $otp لتحديث بياناتك.', module_id='10006', module_name='Resident Services', template_typ_code='receive-otp', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1512' AND lang_code='ara';
UPDATE master.template
SET "name"='Receive OTP', descr='Receive OTP', file_format_code='txt', model='velocity', file_txt='$eventDetails: ನಿಮ್ಮ ಡೇಟಾವನ್ನು ನವೀಕರಿಸಲು OTP $otp ಬಳಸಿ.', module_id='10006', module_name='Resident Services', template_typ_code='receive-otp', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1512' AND lang_code='kan';
UPDATE master.template
SET "name"='Receive OTP', descr='Receive OTP', file_format_code='txt', model='velocity', file_txt='$eventDetails: अपना डेटा अपडेट करने के लिए OTP $otp का उपयोग करें।', module_id='10006', module_name='Resident Services', template_typ_code='receive-otp', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1512' AND lang_code='hin';
UPDATE master.template
SET "name"='Acknwoldegement service history type', descr='Acknwoldegement service history type', file_format_code='txt', model='velocity', file_txt='<!DOCTYPE html>
<html>
   <style>
      table,
      tr,
      td {
      border: 2px solid black;
      word-break: break-word;
      font-family: "Verdana", sans-serif;
      }
	  p {
      word-break: break-word;
      font-family: "Verdana", sans-serif;
      }
   </style>
   <title>home page</title>
   <body>
      <p align=center style=''text-align:center''><span lang=EN-IN><img width=220 height=110 id="Picture 2"
         src="https://mosip.io/images/logo.png" alt="MOSIP"></span></p>
      <table  border=1 cellspacing=0 cellpadding=0 style=''width:90%;margin:auto;border-collapse: collapse; ''>
         <tr>
            <td colspan=2 valign=top
               style=''width:100%;border: solid windowtext 1.0pt; padding: 5.4pt''>
               <p class=MsoListParagraph align=center style=''margin: 0in; text-align: center; line-height: normal''>
                  <b><span lang=EN-IN
                     style=''font-family: "Verdana", sans-serif; color: black; background: white;font-size: 18.0pt;''>Request
                  details</span></b>
               </p>
            </td>
         </tr>
         <tr>
            <td valign=top
               style=''width:20%;border: solid windowtext 1.0pt; border-top: none; padding: 0in 5.4pt 0in 5.4pt''>
               <p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: left; line-height: normal''>
                  <span lang=EN-IN style=''font-size: 14.0pt''>Event request timestamp</span>
               </p>
            </td>
            <td valign=top
               style=''width:80%;border: solid windowtext 1.0pt; border-top: none; padding: 0in 5.4pt 0in 5.4pt''>
               <p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: left; line-height: normal''>
                  <span lang=EN-IN style=''font-size: 14.0pt''>$eventReqTimeStamp</span>
               </p>
            </td>
         </tr>
         <tr>
            <td  valign=top
               style=''width:20%;border: solid windowtext 1.0pt; border-top: none; padding: 0in 5.4pt 0in 5.4pt''>
               <p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: left; line-height: normal''>
                  <span lang=EN-IN style=''font-size: 14.0pt''>Start Date</span>
               </p>
            </td>
            <td  valign=top
               style=''width:80%;border: solid windowtext 1.0pt; border-top: none; padding: 0in 5.4pt 0in 5.4pt''>
               <p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: left; line-height: normal''>
                  <span lang=EN-IN style=''font-size: 14.0pt''>$fromDate</span>
               </p>
            </td>
         </tr>
         <tr>
            <td  valign=top
               style=''width:20%;border: solid windowtext 1.0pt; border-top: none; padding: 0in 5.4pt 0in 5.4pt''>
               <p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: left; line-height: normal''>
                  <span lang=EN-IN style=''font-size: 14.0pt''>End Date</span>
               </p>
            </td>
            <td  valign=top
               style=''width:80%;border: solid windowtext 1.0pt; border-top: none; padding: 0in 5.4pt 0in 5.4pt''>
               <p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: left; line-height: normal''>
                  <span lang=EN-IN style=''font-size: 14.0pt''>$toDate</span>
               </p>
            </td>
         </tr>
         <tr>
            <td  valign=top
               style=''width:20%;border: solid windowtext 1.0pt; border-top: none; padding: 0in 5.4pt 0in 5.4pt''>
               <p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: left; line-height: normal''>
                  <span lang=EN-IN style=''font-size: 14.0pt''>History Type</span>
               </p>
            </td>
            <td  valign=top
               style=''width:80%;border: solid windowtext 1.0pt; border-top: none; padding: 0in 5.4pt 0in 5.4pt''>
               <p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: left; line-height: normal''>
                  <span lang=EN-IN style=''font-size: 14.0pt''>$serviceType</span>
               </p>
            </td>
         </tr>
         <tr>
            <td  valign=top
               style=''width:20%;border: solid windowtext 1.0pt; border-top: none; padding: 0in 5.4pt 0in 5.4pt''>
               <p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: left; line-height: normal''>
                  <span lang=EN-IN style=''font-size: 14.0pt''>Status</span>
               </p>
            </td>
            <td  valign=top
               style=''width:80%;border: solid windowtext 1.0pt; border-top: none; padding: 0in 5.4pt 0in 5.4pt''>
               <p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: left; line-height: normal''>
                  <span lang=EN-IN style=''font-size: 14.0pt''>$statusFilter</span>
               </p>
            </td>
         </tr>
      </table>
      <br><br>
      <table border=1 cellspacing=0 cellpadding=0 style=''width:90%;margin:auto;border-collapse: collapse; ''>
         <tr>
            <td valign=top
               style=''width:5%;border: solid windowtext 1.0pt; border-top: none; padding: 0in 5.4pt 0in 5.4pt''>
               <p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: center; line-height: normal''>
                  <b><span lang=EN-IN style=''font-size: 14.0pt''>S.No</span></b>
               </p>
            </td>
            <td valign=top
               style=''width:20%;border: solid windowtext 1.0pt; border-top: none; padding: 0in 5.4pt 0in 5.4pt''>
               <p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: center; line-height: normal''>
                  <b><span lang=EN-IN style=''font-size: 14.0pt''>Event Id</span></b>
               </p>
            </td>
            <td valign=top
               style=''width:45%;border: solid windowtext 1.0pt; border-top: none; padding: 0in 5.4pt 0in 5.4pt''>
               <p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: center; line-height: normal''>
                  <b><span lang=EN-IN style=''font-size: 14.0pt''>Description</span></b>
               </p>
            </td>
            <td valign=top
               style=''width:15%;border: solid windowtext 1.0pt; border-top: none; padding: 0in 5.4pt 0in 5.4pt''>
               <p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: center; line-height: normal''>
                  <b><span lang=EN-IN style=''font-size: 14.0pt''>Timestamp</span></b>
               </p>
            </td>
            <td valign=top
               style=''width:15%;border: solid windowtext 1.0pt; border-top: none; padding: 0in 5.4pt 0in 5.4pt''>
               <p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: center; line-height: normal''>
                  <b><span lang=EN-IN style=''font-size: 14.0pt''>Status</span></b>
               </p>
            </td>
         </tr>
         #foreach($dto in $serviceHistoryDtlsList)
         <tr>
            <td valign=top
               style=''width:5%;border: solid windowtext 1.0pt; border-top: none; padding: 0in 5.4pt 0in 5.4pt''>
               <p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: left; line-height: normal''>
                  <span lang=EN-IN style=''font-size: 14.0pt''>$dto.serialNumber</span>
               </p>
            </td>
            <td valign=top
               style=''width:20%;border: solid windowtext 1.0pt; border-top: none; padding: 0in 5.4pt 0in 5.4pt''>
               <p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: left; line-height: normal''>
                  <span lang=EN-IN style=''font-size: 14.0pt''>$dto.eventId</span>
               </p>
            </td>
            <td valign=top
               style=''width:45%;border: solid windowtext 1.0pt; border-top: none; padding: 0in 5.4pt 0in 5.4pt''>
               <p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: left; line-height: normal''>
                  <span lang=EN-IN style=''font-size: 14.0pt''>$dto.description</span>
               </p>
            </td>
            <td valign=top
               style=''width:15%;border: solid windowtext 1.0pt; border-top: none; padding: 0in 5.4pt 0in 5.4pt''>
               <p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: left; line-height: normal''>
                  <span lang=EN-IN style=''font-size: 14.0pt''>$dto.timeStamp</span>
               </p>
            </td>
            <td valign=top
               style=''width:15%;border: solid windowtext 1.0pt; border-top: none; padding: 0in 5.4pt 0in 5.4pt''>
               <p class=MsoNormal align=center style=''margin-bottom: 0in; text-align: left; line-height: normal''>
                  <span lang=EN-IN style=''font-size: 14.0pt''>$dto.eventStatus</span>
               </p>
            </td>
         </tr>
         #end
      </table>
	  <br/><br/><br/><br/><br/><br/><br/>
   </body>
</html>', module_id='10006', module_name='Resident Services', template_typ_code='service-history-type', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1514' AND lang_code='eng';
UPDATE master.template
SET "name"='Acknowledgment Authentication Request', descr='Acknowledgment Authentication Request', file_format_code='txt', model='velocity', file_txt='<html>

<head>
    
</head>

<body lang=EN-US link=blue vlink="#954F72" style=''tab-interval:.5in;word-wrap:break-word''>

<div class=WordSection1>

<div>

<p class=MsoNormal align=center style=''text-align:center''><span lang=EN-IN><img
width=200 height=100 id="Picture 2"
src="https://mosip.io/images/logo.png" alt="MOSIP"></span></p>
<p class=MsoNormal align=center style=''text-align:center''><span lang=EN-IN><img
width=50 height=50 id="partnerlogo"
src="$partnerLogo" alt="partner logo"></span></p>

<table class=MsoNormalTable align=center border=0 cellspacing=0 cellpadding=0 width=586
 style=''width:439.4pt;border-collapse:collapse;mso-yfti-tbllook:
 1184;mso-padding-alt:0in 0in 0in 0in''>
 <tr style=''mso-yfti-irow:0;mso-yfti-firstrow:yes''>
  <td width=586 colspan=2 valign=top style=''width:439.4pt;border:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph align=center style=''margin:0in;text-align:center''><b><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Request details</span></b></p>
  </td>
 </tr>
 
 <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event request timestamp: </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$timestamp</span></p></td>
  </tr>
  
  <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event Id:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventId</span></p></td>
  </tr>
  
  <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Authentication mode:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$authenticationMode</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event Type:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventType</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event status:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventStatus</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Summary  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$summary</span></p></td>
  </tr>
</table>

<p class=MsoListParagraph align=center style=''text-align:center''><span
lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
mso-ansi-language:EN-IN''>&nbsp;</span></p>

<p class=MsoListParagraph align=center style=''text-align:center''><span
lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
mso-ansi-language:EN-IN''>To view the status of your event, return to&nbsp;</span><span
lang=EN-IN style=''mso-ansi-language:EN-IN''><a
href=""><span
style=''font-family:"Verdana",sans-serif;color:#004B91;background:white''><a href="$trackServiceRequestLink">Event
Summary</a></span></a></span><span lang=EN-IN style=''font-family:"Verdana",sans-serif;
color:black;background:white;mso-ansi-language:EN-IN''>.</span></p>

<p class=MsoNormal style=''mso-margin-top-alt:auto;mso-margin-bottom-alt:auto''><span
lang=EN-IN style=''mso-ansi-language:EN-IN''>&nbsp;</span></p>

</div>

</div>

</body>

</html>', module_id='10006', module_name='Resident Services', template_typ_code='acknowledgment-authentication-request', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1515' AND lang_code='eng';
UPDATE master.template
SET "name"='Acknowledment Get My Id', descr='Acknowledment Get My Id', file_format_code='txt', model='velocity', file_txt='<html>

<head>
    
</head>

<body lang=EN-US link=blue vlink="#954F72" style=''tab-interval:.5in;word-wrap:break-word''>

<div class=WordSection1>

<div>

<p class=MsoNormal align=center style=''text-align:center''><span lang=EN-IN><img
width=200 height=100 id="Picture 2"
src="https://mosip.io/images/logo.png" alt="MOSIP"></span></p>
<p class=MsoNormal align=center style=''text-align:center''><span lang=EN-IN><img
width=50 height=50 id="partnerlogo"
src="$partnerLogo" alt="partner logo"></span></p>

<table class=MsoNormalTable align=center border=0 cellspacing=0 cellpadding=0 width=586
 style=''width:439.4pt;border-collapse:collapse;mso-yfti-tbllook:
 1184;mso-padding-alt:0in 0in 0in 0in''>
 <tr style=''mso-yfti-irow:0;mso-yfti-firstrow:yes''>
  <td width=586 colspan=2 valign=top style=''width:439.4pt;border:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph align=center style=''margin:0in;text-align:center''><b><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Request details</span></b></p>
  </td>
 </tr>
 
 <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event request timestamp: </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$timestamp</span></p></td>
  </tr>
  
  <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event Id:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventId</span></p></td>
  </tr>
  
  <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Authentication mode:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$authenticationMode</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event Type:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventType</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event status:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventStatus</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Summary  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$summary</span></p></td>
  </tr>
</table>

<p class=MsoListParagraph align=center style=''text-align:center''><span
lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
mso-ansi-language:EN-IN''>&nbsp;</span></p>

<p class=MsoListParagraph align=center style=''text-align:center''><span
lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
mso-ansi-language:EN-IN''>To view the status of your event, return to&nbsp;</span><span
lang=EN-IN style=''mso-ansi-language:EN-IN''><a
href=""><span
style=''font-family:"Verdana",sans-serif;color:#004B91;background:white''><a href="$trackServiceRequestLink">Event
Summary</a></span></a></span><span lang=EN-IN style=''font-family:"Verdana",sans-serif;
color:black;background:white;mso-ansi-language:EN-IN''>.</span></p>

<p class=MsoNormal style=''mso-margin-top-alt:auto;mso-margin-bottom-alt:auto''><span
lang=EN-IN style=''mso-ansi-language:EN-IN''>&nbsp;</span></p>

</div>

</div>

</body>

</html>', module_id='10006', module_name='Resident Services', template_typ_code='acknowledgment-get-my-id', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1516' AND lang_code='eng';
UPDATE master.template
SET "name"='Acknowledgment vid card download', descr='Acknowledgment vid card download', file_format_code='txt', model='velocity', file_txt='<html>

<head>
    
</head>

<body lang=EN-US link=blue vlink="#954F72" style=''tab-interval:.5in;word-wrap:break-word''>

<div class=WordSection1>

<div>

<p class=MsoNormal align=center style=''text-align:center''><span lang=EN-IN><img
width=200 height=100 id="Picture 2"
src="https://mosip.io/images/logo.png" alt="MOSIP"></span></p>
<p class=MsoNormal align=center style=''text-align:center''><span lang=EN-IN><img
width=50 height=50 id="partnerlogo"
src="$partnerLogo" alt="partner logo"></span></p>

<table class=MsoNormalTable align=center border=0 cellspacing=0 cellpadding=0 width=586
 style=''width:439.4pt;border-collapse:collapse;mso-yfti-tbllook:
 1184;mso-padding-alt:0in 0in 0in 0in''>
 <tr style=''mso-yfti-irow:0;mso-yfti-firstrow:yes''>
  <td width=586 colspan=2 valign=top style=''width:439.4pt;border:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph align=center style=''margin:0in;text-align:center''><b><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Request details</span></b></p>
  </td>
 </tr>
 
 <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event request timestamp: </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$timestamp</span></p></td>
  </tr>
  
  <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event Id:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventId</span></p></td>
  </tr>
  
  <tr style=''mso-yfti-irow:1''>
  <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Authentication mode:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$authenticationMode</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event Type:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventType</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Event status:  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$eventStatus</span></p></td>
  </tr>
   <tr style=''mso-yfti-irow:1''>
   <td width=298 valign=top style=''width:223.35pt;border:solid windowtext 1.0pt;
  border-top:none;padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in;text-align:justify''><span
  lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:
  white;mso-ansi-language:EN-IN''>Summary  </span></p></td>
  
  <td width=288 valign=top style=''width:216.05pt;border-top:none;border-left:
  none;border-bottom:solid windowtext 1.0pt;border-right:solid windowtext 1.0pt;
  padding:0in 5.4pt 0in 5.4pt''>
  <p class=MsoListParagraph style=''margin:0in''><span lang=EN-IN
  style=''font-family:"Verdana",sans-serif;color:black;background:white;
  mso-ansi-language:EN-IN''>$summary</span></p></td>
  </tr>
</table>

<p class=MsoListParagraph align=center style=''text-align:center''><span
lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
mso-ansi-language:EN-IN''>&nbsp;</span></p>

<p class=MsoListParagraph align=center style=''text-align:center''><span
lang=EN-IN style=''font-family:"Verdana",sans-serif;color:black;background:white;
mso-ansi-language:EN-IN''>To view the status of your event, return to&nbsp;</span><span
lang=EN-IN style=''mso-ansi-language:EN-IN''><a
href=""><span
style=''font-family:"Verdana",sans-serif;color:#004B91;background:white''><a href="$trackServiceRequestLink">Event
Summary</a></span></a></span><span lang=EN-IN style=''font-family:"Verdana",sans-serif;
color:black;background:white;mso-ansi-language:EN-IN''>.</span></p>

<p class=MsoNormal style=''mso-margin-top-alt:auto;mso-margin-bottom-alt:auto''><span
lang=EN-IN style=''mso-ansi-language:EN-IN''>&nbsp;</span></p>

</div>

</div>

</body>

</html>', module_id='10006', module_name='Resident Services', template_typ_code='acknowledgment-vid-card-download', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1517' AND lang_code='eng';
UPDATE master.template
SET "name"='Vid Card Download Positive Purpose', descr='Vid Card Download Positive Purpose', file_format_code='txt', model='velocity', file_txt='VID card is available to download', module_id='10006', module_name='Resident Services', template_typ_code='vid-card-download-positive-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1518' AND lang_code='eng';
UPDATE master.template
SET "name"='Vid Card Download Negative Purpose', descr='Vid Card Download Negative Purpose', file_format_code='txt', model='velocity', file_txt='An attempt was made to download VID card', module_id='10006', module_name='Resident Services', template_typ_code='vid-card-download-negative-purpose', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1519' AND lang_code='eng';
UPDATE master.template
SET "name"='Vid Card Download Positive Summary', descr='Vid Card Download Positive Summary', file_format_code='txt', model='velocity', file_txt='VID card is available to download', module_id='10006', module_name='Resident Services', template_typ_code='vid-card-download-positive-summary', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1521' AND lang_code='eng';
UPDATE master.template
SET "name"='Phone', descr='Phone', file_format_code='txt', model='velocity', file_txt='Phone', module_id='10006', module_name='Resident Services', template_typ_code='mosip.phone.template.property', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1530' AND lang_code='eng';
UPDATE master.template
SET "name"='Email', descr='Email', file_format_code='txt', model='velocity', file_txt='Email', module_id='10006', module_name='Resident Services', template_typ_code='mosip.email.template.property', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1531' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received email subject to download my VID card', descr='Request received email subject to download my VID card', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='vid-card-download-request-received-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1545' AND lang_code='eng';
UPDATE master.template
SET "name"='Success email subject to download my VID card', descr='Success email subject to download my VID card', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='vid-card-download-success-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1546' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure email subject to download my VID card', descr='Failure email subject to download my VID card', file_format_code='txt', model='velocity', file_txt='Acknowledgement: $eventDetails | event ID: $eventId | Status: $status', module_id='10006', module_name='Resident Services', template_typ_code='vid-card-download-failure-email-subject', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1547' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received email to download my VID card', descr='Request received email to download my VID card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails has been received on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='vid-card-download-request-received-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1548' AND lang_code='eng';
UPDATE master.template
SET "name"='Success email to download my VID card', descr='Success email to download my VID card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails is completed successfully on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='vid-card-download-success-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1549' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure email to download my VID card', descr='Failure email to download my VID card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails raised on $date at $time has failed. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='vid-card-download-failure-email-content', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1550' AND lang_code='eng';
UPDATE master.template
SET "name"='Request received sms to download my VID card', descr='Request received sms to download my VID card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails has been received on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='vid-card-download-request-received_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1551' AND lang_code='eng';
UPDATE master.template
SET "name"='Success sms to download my VID card', descr='Success sms to download my VID card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails is completed successfully on $date at $time. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='vid-card-download-success_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1552' AND lang_code='eng';
UPDATE master.template
SET "name"='Failure sms to download my VID card', descr='Failure sms to download my VID card', file_format_code='txt', model='velocity', file_txt='Dear $name, <br>
Your request to $eventDetails raised on $date at $time has failed. <br>
Your event id is #$eventId. <br>
Log in to the official website $trackServiceRequestLink for further details. <br>', module_id='10006', module_name='Resident Services', template_typ_code='vid-card-download-failure_SMS', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1553' AND lang_code='eng';
UPDATE master.template
SET "name"='Email otp', descr='Email otp', file_format_code='txt', model='velocity', file_txt='Email OTP', module_id='10006', module_name='Resident Services', template_typ_code='mosip.otp-email.template.property', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1582' AND lang_code='eng';
UPDATE master.template
SET "name"='Phone otp', descr='Phone otp', file_format_code='txt', model='velocity', file_txt='Phone OTP', module_id='10006', module_name='Resident Services', template_typ_code='mosip.otp-phone.template.property', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1583' AND lang_code='eng';
UPDATE master.template
SET "name"='Demographic', descr='Demographic', file_format_code='txt', model='velocity', file_txt='Demographic', module_id='10006', module_name='Resident Services', template_typ_code='mosip.demo.template.property', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1584' AND lang_code='eng';
UPDATE master.template
SET "name"='Finger bio', descr='Finger bio', file_format_code='txt', model='velocity', file_txt='Fingerprint', module_id='10006', module_name='Resident Services', template_typ_code='mosip.bio-finger.template.property', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1585' AND lang_code='eng';
UPDATE master.template
SET "name"='Iris bio', descr='Iris bio', file_format_code='txt', model='velocity', file_txt='Iris', module_id='10006', module_name='Resident Services', template_typ_code='mosip.bio-iris.template.property', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1586' AND lang_code='eng';
UPDATE master.template
SET "name"='Face bio', descr='Face bio', file_format_code='txt', model='velocity', file_txt='Face', module_id='10006', module_name='Resident Services', template_typ_code='mosip.bio-face.template.property', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1587' AND lang_code='eng';
UPDATE master.template
SET "name"='Unlocked status', descr='Unlocked status', file_format_code='txt', model='velocity', file_txt='unlocked', module_id='10006', module_name='Resident Services', template_typ_code='mosip.unlocked.template.property', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1588' AND lang_code='eng';
UPDATE master.template
SET "name"='Locked status', descr='Locked status', file_format_code='txt', model='velocity', file_txt='locked', module_id='10006', module_name='Resident Services', template_typ_code='mosip.locked.template.property', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='1589' AND lang_code='eng';
UPDATE master.template
SET "name"='Vid Card Type', descr='Vid Card Type', file_format_code='txt', model='velocity', file_txt='<html>

<head>
<meta http-equiv=Content-Type content=text/html; charset=windows-1252>
<meta name=Generator content=Microsoft Word 15 (filtered)>
<style>
<!--
 /* Font Definitions */
 @font-face
	{font-family:Cambria Math;
	panose-1:2 4 5 3 5 4 6 3 2 4;}
@font-face
	{font-family:Calibri;
	panose-1:2 15 5 2 2 2 4 3 2 4;}
 /* Style Definitions */
 p.MsoNormal, li.MsoNormal, div.MsoNormal
	{margin-top:0in;
	margin-right:0in;
	margin-bottom:8.0pt;
	margin-left:0in;
	line-height:107%;
	font-size:11.0pt;
	font-family:Calibri,sans-serif;}
.MsoChpDefault
	{font-family:Calibri,sans-serif;}
.MsoPapDefault
	{margin-bottom:8.0pt;
	line-height:107%;}
 /* Page Definitions */
 @page WordSection1
	{size:595.3pt 841.9pt;
	margin:1.0in 1.0in 1.0in 1.0in;}
div.WordSection1
	{page:WordSection1;}
-->
</style>

</head>

<body lang=EN-US style=''word-wrap:break-word''>

<div class=WordSection1>

<p class=MsoNormal style=''line-height:normal''><span style=''position:relative;
z-index:251659264''><span style=''position:absolute;left:-3px;top:-1px;
width:352px;height:183px''><img width=352 height=183
src=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAWAAAAC3CAMAAAAb83RdAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAABjUExURQAAAECAn0Bwn0B1n0Bwn0BzmUBwmkBym0Bwm0BxnEBwnEBxnEBwnUBxmkBwm0Bxm0Jym0JxnUJynUJxnUJynUJxnEJynEFxnEFxnEFxnEFxnEFxnUFxnUFxnEFxnEFxnEFxnGJA1PgAAAAgdFJOUwAIEBggKDA4QEhQWGBocHiAh4+Xn6evt7/Hz9ff5+/3v+H4wgAAAAlwSFlzAAAOwwAADsMBx2+oZAAABGxJREFUeF7t2sli2jAURmFjAzHzECAGDOb9n7K68s3UQDBB/+58C8BqujlVZckku6FXTperqjpfcEdTVa/LeVl4uE7yybbxv46uqlnf890z3flfwYMO89wb/mJ88J/GH5zmPe94Q7n3n7zU2+VyVA58HDcVZblYvn5My3ri49fk74vDcTX0IXTUn1ce73BzLe4f25/YMm//pFi2W4Nz6QP/eWn3ZG9M3j8r1jFhM/PrbyYx//5GfXTT38bEa7/8YhL/YHvnJoi75jHkq199KOP8XfgVnjCKS+3cr1zfBpuxX+Epg9oKj/wqyuP+gb6JxOl6/roXe7O+rA/JlNaz/ryfje166xdIIN7pPpdhO+ft2T+ktAlJm/dHmHECc7xIqrBdme+GezaBd+1npGKLhE/hGRNYoHcKVdv7mj0F2sRPSMjmbWM3tsImcNdvO9CdHTfsaDEN7/t2CCnZkzVbGeyQwRlDwE4bp3BKtv0ET9gFenZgLuNjyqMPISk7bCzifu3Hw0ukYPuITbYKr0sfQVKjkHYX5/HUR5DUMKTdx2PGt2fDSMVOGHVmj9o5J2uEtJfMdmkP/WYgOrN9WlsZErEtgXUILEZgMQKLEViMwGIEFiOwGIHFCCxGYDECixFYjMBiBBYjsBiBxQgsRmAxAosRWIzAYgQWI7AYgcUILEZgMQKLEViMwGIEFiOwGIHFCCxGYDECixFYjMBiBBYjsBiBxQgsRmAxAosRWIzAYgQWI7AYgcUILEZgMQKLEViMwGIEFiOwGIHFCCxGYDECixFYjMBiBBYjsBiBxQgsRmAxAosRWIzAYgQWI7AYgcUILEZgMQKLEViMwGIEFiOwGIHFCCxGYDECixFYjMBiBBYjsBiBxQgsRmAxAosRWIzAYgQWI7AYgcUILEZgMQKLEViMwGIEFiOwGIHFCCxGYDECixFYjMBiBBYjsBiBxQgsRmAxAosRWIzAYgQWI7DYR+CeDyCtxgLX4aXvA0grBt6Hl9IHkFQe0p6yXXid+AiSGoS0x2wdXuc+gqTKkLbKFuF15SNIahrSbuLrm48gqVWcu7ZQNLkPIaVjSBtub/Y29SEk1PepuwzvWx9DQvMQtgrvLxaas1x6VQg7sw92lmOjltwwZG3PyHazOzGFU7MT3D5+KuzIvIgfkczYJvC4/WyHuaZoPyORQ4hqtziTn8LF2i+QxMQm8NAv4obifTojhYEtuzu/yLKeTeHmozeeldvOrPnymP3Fgtcsw4n0bAv8/Xgcl4wDjyTS2FjN/25qthm+1KwSCeRx/v54Qmn74kvDne5pA1t/L/WP1aD3ZuOXJUe650zOse+175HtvBEOzTy6fEJp3yGHE8b1u9nM9hLhXjfyazxoENfZXw5tZZzeIfFi4CPorJh53iY+o7yuaJeJoF6PSn4fpaO8LBf27CHa/T45+/7P4OyIh1+d/b99a3//l3iG7XYCf3Dsts0tprv2dodH7OcP3Ll642VV2ffN6OBUVevp1Sc5WfYPwR32gG13uhEAAAAASUVORK5CYII=>
<img width=41 height=44
src=https://mosip.io/images/logo.png alt=mosip align=right hspace=12 style=margin-top:-172px;></span></span>
<span style=margin-left: 16px;><b>Id type:</b> VID
card</p></span>

<p class=MsoNormal style=''margin-top:0in;margin-right:0in;margin-bottom:0in;
line-height:normal''><img width=84 height=84
src=$image alt=user image align=left hspace=12><b>Name:</b> $name</p>

<p class=MsoNormal style=''margin-top:0in;margin-right:0in;margin-bottom:0in;
line-height:normal''><b>DOB:</b> $dateOfBirth</p>

<p class=MsoNormal style=''margin-top:0in;margin-right:0in;margin-bottom:0in;
line-height:normal''><b>VID:</b> $vid</p>

<p class=MsoNormal style=''margin-top:0in;margin-right:0in;margin-bottom:0in;
line-height:normal''><b>VID Type:</b> $vidType</p>

<p class=MsoNormal style=''margin-top:0in;margin-right:0in;margin-bottom:0in;
line-height:normal''><b>Generated on:</b> $genratedOnTimestamp</p>

<p class=MsoNormal style=''margin-top:0in;margin-right:0in;margin-bottom:0in;
margin-left:1.12in;line-height:normal''><b>Expires on:</b> $expiryTimestamp</p>

<p class=MsoNormal style=''margin-top:0in;margin-right:0in;margin-bottom:0in;
margin-left:1.1in;line-height:normal''><b>Transactions left:</b> $transactionsLeftCount</p>

</div>

</body>

</html>', module_id='10006', module_name='Resident Services', template_typ_code='vid-card-type', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:58.127', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL
WHERE id='2001' AND lang_code='eng';

UPDATE master.template_type
SET code='RS_AUTH_HIST_SUCCESS_EMAIL', descr='Authentication History Request Success Email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_SUCCESS_SMS', descr='Authentication History Request Success SMS', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_SUCCESS_EMAIL_SUB', descr='Authentication History Request Success EMAIL Subject', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_SUCCESS_EMAIL', descr='Successful Download of e-UIN Email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_SUCCESS_EMAIL_SUB', descr='Download e-UIN Status Email Subject', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_SUCCESS_SMS', descr='Successful Download of e-UIN SMS', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_SUCCESS_EMAIL', descr='Successful Locking of Auth Types Email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_SUCCESS_EMAIL_SUB', descr='Successful Locking of Auth Types Email Subject', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_SUCCESS_SMS', descr='Successful Locking of Auth Types SMS', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_SUCCESS_EMAIL', descr='Successful Unlocking of Auth Types Email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_SUCCESS_EMAIL_SUB', descr='Successful Unlocking of Auth Types Email Subject', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_SUCCESS_SMS', descr='Successful Unlocking of Auth Types SMS', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_SUCCESS_EMAIL', descr='VID Generation Success Email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_SUCCESS_EMAIL_SUB', descr='VID Generation Success Email Subject', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_SUCCESS_SMS', descr='VID Generation Success SMS', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_SUCCESS_EMAIL', descr='VID Revocation Success Email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_SUCCESS_EMAIL_SUB', descr='VID Revocation Success Email Subject', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_SUCCESS_SMS', descr='VID Revocation Success SMS', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_SUCCESS_EMAIL', descr='Reprint Request Success Email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_SUCCESS_EMAIL_SUB', descr='Reprint Request Success Email Subject', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_SUCCESS_SMS', descr='Reprint Request Success SMS', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_FAILURE_EMAIL', descr='Authentication History Request Failure Email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_FAILURE_EMAIL_SUB', descr='Authentication History Request Failure Email Subject', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_FAILURE_SMS', descr='Authentication History Request Failure SMS', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_FAILURE_EMAIL', descr='Download e-UIN Failure Email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_FAILURE_EMAIL_SUB', descr='Download e-UIN Failure Email Subject', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_FAILURE_SMS', descr='Download e-UIN Failure SMS', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_FAILURE_EMAIL', descr='Failure in Locking of Auth Types Email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_FAILURE_EMAIL_SUB', descr='Failure in Locking of Auth Types Email Subject', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_FAILURE_SMS', descr='Failure in Locking of Auth Types SMS', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_FAILURE_EMAIL', descr='Reprint Request Failure Email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_FAILURE_EMAIL_SUB', descr='Reprint Request Failure Email Subject', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_FAILURE_SMS', descr='Reprint Request Failure SMS', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_FAILURE_EMAIL', descr='Failure in Unlocking of Auth Types Email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_FAILURE_EMAIL_SUB', descr='Failure in Unlocking of Auth Types Email Subject', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_FAILURE_SMS', descr='Failure in Unlocking of Auth Types SMS', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_FAILURE_EMAIL', descr='VID Generation Failure Email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_FAILURE_EMAIL_SUB', descr='VID Generation Failure Email Subject', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_FAILURE_SMS', descr='VID Generation Failure SMS', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_FAILURE_EMAIL', descr='VID Revocation Failure Email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_FAILURE_EMAIL_SUB', descr='VID Revocation Failure Email Subject', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_FAILURE_SMS', descr='VID Revocation Failure SMS', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_SUCCESS_EMAIL', descr='UIN Update Request Placed Successfully Email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_SUCCESS_EMAIL_SUB', descr='UIN Update Request Placed Successfully Email Subject', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_SUCCESS_SMS', descr='UIN Update Request Placed Successfully SMS', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_FAILURE_EMAIL', descr='UIN Update Request Failed Email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_FAILURE_EMAIL_SUB', descr='UIN Update Request Failed Email Subject', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_FAILURE_SMS', descr='UIN Update Request Failed SMS', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_SUCCESS_SMS', descr='Credential Issuance Success SMS', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_SUCCESS_EMAIL', descr='Credential Issuance Success EMAIL', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_SUCCESS_EMAIL_SUB', descr='Credential Issuance Success EMAIL Subject', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_STATUS_SMS', descr='Credential Issuance Status Check SMS', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_STATUS_EMAIL', descr='Credential Issuance Status Check EMAIL', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_STATUS_EMAIL_SUB', descr='Credential Issuance Status Check EMAIL Subject', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_CANCEL_SUCCESS_SMS', descr='Credential Request Cancel Success SMS', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_CANCEL_SUCCESS_EMAIL', descr='Credential Request Cancel Success EMAIL', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_CANCEL_SUCCESS_EMAIL_SUB', descr='Credential Request Cancel Success EMAIL Subject', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_FAILURE_SMS', descr='Credential Issuance Failure SMS', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_FAILURE_EMAIL', descr='Credential Issuance Failure EMAIL', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_FAILURE_EMAIL_SUB', descr='Credential Issuance Failure EMAIL Subject', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_SUCCESS_EMAIL', descr='Authentication History Request Success Email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_SUCCESS_SMS', descr='Authentication History Request Success SMS', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_SUCCESS_EMAIL_SUB', descr='Authentication History Request Success EMAIL Subject', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_SUCCESS_EMAIL', descr='Successful Download of e-UIN Email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_SUCCESS_EMAIL_SUB', descr='Download e-UIN Status Email Subject', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_SUCCESS_SMS', descr='Successful Download of e-UIN SMS', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_SUCCESS_EMAIL', descr='Successful Locking of Auth Types Email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_SUCCESS_EMAIL_SUB', descr='Successful Locking of Auth Types Email Subject', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_SUCCESS_SMS', descr='Successful Locking of Auth Types SMS', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_SUCCESS_EMAIL', descr='Successful Unlocking of Auth Types Email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_SUCCESS_EMAIL_SUB', descr='Successful Unlocking of Auth Types Email Subject', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_SUCCESS_SMS', descr='Successful Unlocking of Auth Types SMS', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_SUCCESS_EMAIL', descr='VID Generation Success Email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_SUCCESS_EMAIL_SUB', descr='VID Generation Success Email Subject', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_SUCCESS_SMS', descr='VID Generation Success SMS', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_SUCCESS_EMAIL', descr='VID Revocation Success Email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_SUCCESS_EMAIL_SUB', descr='VID Revocation Success Email Subject', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_SUCCESS_SMS', descr='VID Revocation Success SMS', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_SUCCESS_EMAIL', descr='Reprint Request Success Email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_SUCCESS_EMAIL_SUB', descr='Reprint Request Success Email Subject', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_SUCCESS_SMS', descr='Reprint Request Success SMS', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_FAILURE_EMAIL', descr='Authentication History Request Failure Email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_FAILURE_EMAIL_SUB', descr='Authentication History Request Failure Email Subject', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_FAILURE_SMS', descr='Authentication History Request Failure SMS', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_FAILURE_EMAIL', descr='Download e-UIN Failure Email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_FAILURE_EMAIL_SUB', descr='Download e-UIN Failure Email Subject', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_FAILURE_SMS', descr='Download e-UIN Failure SMS', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_FAILURE_EMAIL', descr='Failure in Locking of Auth Types Email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_FAILURE_EMAIL_SUB', descr='Failure in Locking of Auth Types Email Subject', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_FAILURE_SMS', descr='Failure in Locking of Auth Types SMS', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_FAILURE_EMAIL', descr='Reprint Request Failure Email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_FAILURE_EMAIL_SUB', descr='Reprint Request Failure Email Subject', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_FAILURE_SMS', descr='Reprint Request Failure SMS', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_FAILURE_EMAIL', descr='Failure in Unlocking of Auth Types Email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_FAILURE_EMAIL_SUB', descr='Failure in Unlocking of Auth Types Email Subject', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_FAILURE_SMS', descr='Failure in Unlocking of Auth Types SMS', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_FAILURE_EMAIL', descr='VID Generation Failure Email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_FAILURE_EMAIL_SUB', descr='VID Generation Failure Email Subject', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_FAILURE_SMS', descr='VID Generation Failure SMS', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_FAILURE_EMAIL', descr='VID Revocation Failure Email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_FAILURE_EMAIL_SUB', descr='VID Revocation Failure Email Subject', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_FAILURE_SMS', descr='VID Revocation Failure SMS', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_SUCCESS_EMAIL', descr='UIN Update Request Placed Successfully Email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_SUCCESS_EMAIL_SUB', descr='UIN Update Request Placed Successfully Email Subject', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_SUCCESS_SMS', descr='UIN Update Request Placed Successfully SMS', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_FAILURE_EMAIL', descr='UIN Update Request Failed Email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_FAILURE_EMAIL_SUB', descr='UIN Update Request Failed Email Subject', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_FAILURE_SMS', descr='UIN Update Request Failed SMS', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='receive-otp', descr='Receive OTP', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_SUCCESS_SMS', descr='Credential Issuance Success SMS', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_SUCCESS_EMAIL', descr='Credential Issuance Success EMAIL', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_SUCCESS_EMAIL_SUB', descr='Credential Issuance Success EMAIL Subject', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_STATUS_SMS', descr='Credential Issuance Status Check SMS', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_STATUS_EMAIL', descr='Credential Issuance Status Check EMAIL', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_STATUS_EMAIL_SUB', descr='Credential Issuance Status Check EMAIL Subject', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_CANCEL_SUCCESS_SMS', descr='Credential Request Cancel Success SMS', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_CANCEL_SUCCESS_EMAIL', descr='Credential Request Cancel Success EMAIL', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_CANCEL_SUCCESS_EMAIL_SUB', descr='Credential Request Cancel Success EMAIL Subject', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_FAILURE_SMS', descr='Credential Issuance Failure SMS', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_FAILURE_EMAIL', descr='Credential Issuance Failure EMAIL', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_FAILURE_EMAIL_SUB', descr='Credential Issuance Failure EMAIL Subject', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_SUCCESS_EMAIL', descr='Authentication History Request Success Email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_SUCCESS_SMS', descr='Authentication History Request Success SMS', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_SUCCESS_EMAIL_SUB', descr='Authentication History Request Success EMAIL Subject', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_SUCCESS_EMAIL', descr='Successful Download of e-UIN Email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_SUCCESS_EMAIL_SUB', descr='Download e-UIN Status Email Subject', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_SUCCESS_SMS', descr='Successful Download of e-UIN SMS', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_SUCCESS_EMAIL', descr='Successful Locking of Auth Types Email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_SUCCESS_EMAIL_SUB', descr='Successful Locking of Auth Types Email Subject', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_SUCCESS_SMS', descr='Successful Locking of Auth Types SMS', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_SUCCESS_EMAIL', descr='Successful Unlocking of Auth Types Email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_SUCCESS_EMAIL_SUB', descr='Successful Unlocking of Auth Types Email Subject', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_SUCCESS_SMS', descr='Successful Unlocking of Auth Types SMS', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_SUCCESS_EMAIL', descr='VID Generation Success Email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_SUCCESS_EMAIL_SUB', descr='VID Generation Success Email Subject', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_SUCCESS_SMS', descr='VID Generation Success SMS', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_SUCCESS_EMAIL', descr='VID Revocation Success Email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_SUCCESS_EMAIL_SUB', descr='VID Revocation Success Email Subject', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_SUCCESS_SMS', descr='VID Revocation Success SMS', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_SUCCESS_EMAIL', descr='Reprint Request Success Email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_SUCCESS_EMAIL_SUB', descr='Reprint Request Success Email Subject', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_SUCCESS_SMS', descr='Reprint Request Success SMS', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_FAILURE_EMAIL', descr='Authentication History Request Failure Email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_FAILURE_EMAIL_SUB', descr='Authentication History Request Failure Email Subject', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_FAILURE_SMS', descr='Authentication History Request Failure SMS', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_FAILURE_EMAIL', descr='Download e-UIN Failure Email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_FAILURE_EMAIL_SUB', descr='Download e-UIN Failure Email Subject', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_FAILURE_SMS', descr='Download e-UIN Failure SMS', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_FAILURE_EMAIL', descr='Failure in Locking of Auth Types Email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_FAILURE_EMAIL_SUB', descr='Failure in Locking of Auth Types Email Subject', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_FAILURE_SMS', descr='Failure in Locking of Auth Types SMS', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_FAILURE_EMAIL', descr='Reprint Request Failure Email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_FAILURE_EMAIL_SUB', descr='Reprint Request Failure Email Subject', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_FAILURE_SMS', descr='Reprint Request Failure SMS', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_FAILURE_EMAIL', descr='Failure in Unlocking of Auth Types Email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_FAILURE_EMAIL_SUB', descr='Failure in Unlocking of Auth Types Email Subject', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_FAILURE_SMS', descr='Failure in Unlocking of Auth Types SMS', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_FAILURE_EMAIL', descr='VID Generation Failure Email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_FAILURE_EMAIL_SUB', descr='VID Generation Failure Email Subject', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_FAILURE_SMS', descr='VID Generation Failure SMS', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_FAILURE_EMAIL', descr='VID Revocation Failure Email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_FAILURE_EMAIL_SUB', descr='VID Revocation Failure Email Subject', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_FAILURE_SMS', descr='VID Revocation Failure SMS', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_SUCCESS_EMAIL', descr='UIN Update Request Placed Successfully Email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_SUCCESS_EMAIL_SUB', descr='UIN Update Request Placed Successfully Email Subject', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_SUCCESS_SMS', descr='UIN Update Request Placed Successfully SMS', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_FAILURE_EMAIL', descr='UIN Update Request Failed Email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_FAILURE_EMAIL_SUB', descr='UIN Update Request Failed Email Subject', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_FAILURE_SMS', descr='UIN Update Request Failed SMS', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_SUCCESS_SMS', descr='Credential Issuance Success SMS', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_SUCCESS_EMAIL', descr='Credential Issuance Success EMAIL', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_SUCCESS_EMAIL_SUB', descr='Credential Issuance Success EMAIL Subject', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_STATUS_SMS', descr='Credential Issuance Status Check SMS', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_STATUS_EMAIL', descr='Credential Issuance Status Check EMAIL', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_STATUS_EMAIL_SUB', descr='Credential Issuance Status Check EMAIL Subject', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_CANCEL_SUCCESS_SMS', descr='Credential Request Cancel Success SMS', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_CANCEL_SUCCESS_EMAIL', descr='Credential Request Cancel Success EMAIL', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_CANCEL_SUCCESS_EMAIL_SUB', descr='Credential Request Cancel Success EMAIL Subject', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_FAILURE_SMS', descr='Credential Issuance Failure SMS', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_FAILURE_EMAIL', descr='Credential Issuance Failure EMAIL', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_FAILURE_EMAIL_SUB', descr='Credential Issuance Failure EMAIL Subject', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_SUCCESS_EMAIL', descr='Authentication History Request Success Email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_SUCCESS_SMS', descr='Authentication History Request Success SMS', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_SUCCESS_EMAIL_SUB', descr='Authentication History Request Success EMAIL Subject', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_SUCCESS_EMAIL', descr='Successful Download of e-UIN Email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_SUCCESS_EMAIL_SUB', descr='Download e-UIN Status Email Subject', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_SUCCESS_SMS', descr='Successful Download of e-UIN SMS', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_SUCCESS_EMAIL', descr='Successful Locking of Auth Types Email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_SUCCESS_EMAIL_SUB', descr='Successful Locking of Auth Types Email Subject', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_SUCCESS_SMS', descr='Successful Locking of Auth Types SMS', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_SUCCESS_EMAIL', descr='Successful Unlocking of Auth Types Email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_SUCCESS_EMAIL_SUB', descr='Successful Unlocking of Auth Types Email Subject', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_SUCCESS_SMS', descr='Successful Unlocking of Auth Types SMS', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_SUCCESS_EMAIL', descr='VID Generation Success Email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_SUCCESS_EMAIL_SUB', descr='VID Generation Success Email Subject', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_SUCCESS_SMS', descr='VID Generation Success SMS', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_SUCCESS_EMAIL', descr='VID Revocation Success Email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_SUCCESS_EMAIL_SUB', descr='VID Revocation Success Email Subject', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_SUCCESS_SMS', descr='VID Revocation Success SMS', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_SUCCESS_EMAIL', descr='Reprint Request Success Email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_SUCCESS_EMAIL_SUB', descr='Reprint Request Success Email Subject', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_SUCCESS_SMS', descr='Reprint Request Success SMS', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_FAILURE_EMAIL', descr='Authentication History Request Failure Email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_FAILURE_EMAIL_SUB', descr='Authentication History Request Failure Email Subject', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_FAILURE_SMS', descr='Authentication History Request Failure SMS', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_FAILURE_EMAIL', descr='Download e-UIN Failure Email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_FAILURE_EMAIL_SUB', descr='Download e-UIN Failure Email Subject', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_FAILURE_SMS', descr='Download e-UIN Failure SMS', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_FAILURE_EMAIL', descr='Failure in Locking of Auth Types Email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_FAILURE_EMAIL_SUB', descr='Failure in Locking of Auth Types Email Subject', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_FAILURE_SMS', descr='Failure in Locking of Auth Types SMS', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_FAILURE_EMAIL', descr='Reprint Request Failure Email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_FAILURE_EMAIL_SUB', descr='Reprint Request Failure Email Subject', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_FAILURE_SMS', descr='Reprint Request Failure SMS', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_FAILURE_EMAIL', descr='Failure in Unlocking of Auth Types Email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_FAILURE_EMAIL_SUB', descr='Failure in Unlocking of Auth Types Email Subject', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_FAILURE_SMS', descr='Failure in Unlocking of Auth Types SMS', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_FAILURE_EMAIL', descr='VID Generation Failure Email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_FAILURE_EMAIL_SUB', descr='VID Generation Failure Email Subject', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_FAILURE_SMS', descr='VID Generation Failure SMS', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_FAILURE_EMAIL', descr='VID Revocation Failure Email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_FAILURE_EMAIL_SUB', descr='VID Revocation Failure Email Subject', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_FAILURE_SMS', descr='VID Revocation Failure SMS', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_SUCCESS_EMAIL', descr='UIN Update Request Placed Successfully Email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_SUCCESS_EMAIL_SUB', descr='UIN Update Request Placed Successfully Email Subject', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_SUCCESS_SMS', descr='UIN Update Request Placed Successfully SMS', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_FAILURE_EMAIL', descr='UIN Update Request Failed Email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_FAILURE_EMAIL_SUB', descr='UIN Update Request Failed Email Subject', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_FAILURE_SMS', descr='UIN Update Request Failed SMS', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_SUCCESS_SMS', descr='Credential Issuance Success SMS', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_SUCCESS_EMAIL', descr='Credential Issuance Success EMAIL', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_SUCCESS_EMAIL_SUB', descr='Credential Issuance Success EMAIL Subject', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_STATUS_SMS', descr='Credential Issuance Status Check SMS', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_STATUS_EMAIL', descr='Credential Issuance Status Check EMAIL', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_STATUS_EMAIL_SUB', descr='Credential Issuance Status Check EMAIL Subject', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_CANCEL_SUCCESS_SMS', descr='Credential Request Cancel Success SMS', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_CANCEL_SUCCESS_EMAIL', descr='Credential Request Cancel Success EMAIL', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_CANCEL_SUCCESS_EMAIL_SUB', descr='Credential Request Cancel Success EMAIL Subject', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_FAILURE_SMS', descr='Credential Issuance Failure SMS', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_FAILURE_EMAIL', descr='Credential Issuance Failure EMAIL', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_FAILURE_EMAIL_SUB', descr='Credential Issuance Failure EMAIL Subject', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_SUCCESS_EMAIL', descr='Authentication History Request Success Email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_SUCCESS_SMS', descr='Authentication History Request Success SMS', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_SUCCESS_EMAIL_SUB', descr='Authentication History Request Success EMAIL Subject', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_SUCCESS_EMAIL', descr='Successful Download of e-UIN Email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_SUCCESS_EMAIL_SUB', descr='Download e-UIN Status Email Subject', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_SUCCESS_SMS', descr='Successful Download of e-UIN SMS', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_SUCCESS_EMAIL', descr='Successful Locking of Auth Types Email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_SUCCESS_EMAIL_SUB', descr='Successful Locking of Auth Types Email Subject', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_SUCCESS_SMS', descr='Successful Locking of Auth Types SMS', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_SUCCESS_EMAIL', descr='Successful Unlocking of Auth Types Email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_SUCCESS_EMAIL_SUB', descr='Successful Unlocking of Auth Types Email Subject', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_SUCCESS_SMS', descr='Successful Unlocking of Auth Types SMS', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_SUCCESS_EMAIL', descr='VID Generation Success Email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_SUCCESS_EMAIL_SUB', descr='VID Generation Success Email Subject', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_SUCCESS_SMS', descr='VID Generation Success SMS', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_SUCCESS_EMAIL', descr='VID Revocation Success Email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_SUCCESS_EMAIL_SUB', descr='VID Revocation Success Email Subject', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_SUCCESS_SMS', descr='VID Revocation Success SMS', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_SUCCESS_EMAIL', descr='Reprint Request Success Email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_SUCCESS_EMAIL_SUB', descr='Reprint Request Success Email Subject', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_SUCCESS_SMS', descr='Reprint Request Success SMS', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_FAILURE_EMAIL', descr='Authentication History Request Failure Email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_FAILURE_EMAIL_SUB', descr='Authentication History Request Failure Email Subject', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_FAILURE_SMS', descr='Authentication History Request Failure SMS', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_FAILURE_EMAIL', descr='Download e-UIN Failure Email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_FAILURE_EMAIL_SUB', descr='Download e-UIN Failure Email Subject', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_FAILURE_SMS', descr='Download e-UIN Failure SMS', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_FAILURE_EMAIL', descr='Failure in Locking of Auth Types Email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_FAILURE_EMAIL_SUB', descr='Failure in Locking of Auth Types Email Subject', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_FAILURE_SMS', descr='Failure in Locking of Auth Types SMS', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_FAILURE_EMAIL', descr='Reprint Request Failure Email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_FAILURE_EMAIL_SUB', descr='Reprint Request Failure Email Subject', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_FAILURE_SMS', descr='Reprint Request Failure SMS', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_FAILURE_EMAIL', descr='Failure in Unlocking of Auth Types Email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_FAILURE_EMAIL_SUB', descr='Failure in Unlocking of Auth Types Email Subject', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_FAILURE_SMS', descr='Failure in Unlocking of Auth Types SMS', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_FAILURE_EMAIL', descr='VID Generation Failure Email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_FAILURE_EMAIL_SUB', descr='VID Generation Failure Email Subject', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_FAILURE_SMS', descr='VID Generation Failure SMS', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_FAILURE_EMAIL', descr='VID Revocation Failure Email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_FAILURE_EMAIL_SUB', descr='VID Revocation Failure Email Subject', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_FAILURE_SMS', descr='VID Revocation Failure SMS', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_SUCCESS_EMAIL', descr='UIN Update Request Placed Successfully Email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_SUCCESS_EMAIL_SUB', descr='UIN Update Request Placed Successfully Email Subject', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_SUCCESS_SMS', descr='UIN Update Request Placed Successfully SMS', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_FAILURE_EMAIL', descr='UIN Update Request Failed Email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_FAILURE_EMAIL_SUB', descr='UIN Update Request Failed Email Subject', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_FAILURE_SMS', descr='UIN Update Request Failed SMS', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_SUCCESS_SMS', descr='Credential Issuance Success SMS', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_SUCCESS_EMAIL', descr='Credential Issuance Success EMAIL', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_SUCCESS_EMAIL_SUB', descr='Credential Issuance Success EMAIL Subject', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_STATUS_SMS', descr='Credential Issuance Status Check SMS', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_STATUS_EMAIL', descr='Credential Issuance Status Check EMAIL', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_STATUS_EMAIL_SUB', descr='Credential Issuance Status Check EMAIL Subject', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_CANCEL_SUCCESS_SMS', descr='Credential Request Cancel Success SMS', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_CANCEL_SUCCESS_EMAIL', descr='Credential Request Cancel Success EMAIL', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_CANCEL_SUCCESS_EMAIL_SUB', descr='Credential Request Cancel Success EMAIL Subject', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_FAILURE_SMS', descr='Credential Issuance Failure SMS', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_FAILURE_EMAIL', descr='Credential Issuance Failure EMAIL', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_FAILURE_EMAIL_SUB', descr='Credential Issuance Failure EMAIL Subject', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_SUCCESS_EMAIL', descr='Authentication History Request Success Email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_SUCCESS_SMS', descr='Authentication History Request Success SMS', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_SUCCESS_EMAIL_SUB', descr='Authentication History Request Success EMAIL Subject', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_SUCCESS_EMAIL', descr='Successful Download of e-UIN Email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_SUCCESS_EMAIL_SUB', descr='Download e-UIN Status Email Subject', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_SUCCESS_SMS', descr='Successful Download of e-UIN SMS', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_SUCCESS_EMAIL', descr='Successful Locking of Auth Types Email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_SUCCESS_EMAIL_SUB', descr='Successful Locking of Auth Types Email Subject', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_SUCCESS_SMS', descr='Successful Locking of Auth Types SMS', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_SUCCESS_EMAIL', descr='Successful Unlocking of Auth Types Email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_SUCCESS_EMAIL_SUB', descr='Successful Unlocking of Auth Types Email Subject', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_SUCCESS_SMS', descr='Successful Unlocking of Auth Types SMS', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_SUCCESS_EMAIL', descr='VID Generation Success Email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_SUCCESS_EMAIL_SUB', descr='VID Generation Success Email Subject', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_SUCCESS_SMS', descr='VID Generation Success SMS', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_SUCCESS_EMAIL', descr='VID Revocation Success Email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_SUCCESS_EMAIL_SUB', descr='VID Revocation Success Email Subject', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_SUCCESS_SMS', descr='VID Revocation Success SMS', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_SUCCESS_EMAIL', descr='Reprint Request Success Email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_SUCCESS_EMAIL_SUB', descr='Reprint Request Success Email Subject', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_SUCCESS_SMS', descr='Reprint Request Success SMS', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_FAILURE_EMAIL', descr='Authentication History Request Failure Email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_FAILURE_EMAIL_SUB', descr='Authentication History Request Failure Email Subject', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_AUTH_HIST_FAILURE_SMS', descr='Authentication History Request Failure SMS', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_FAILURE_EMAIL', descr='Download e-UIN Failure Email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_FAILURE_EMAIL_SUB', descr='Download e-UIN Failure Email Subject', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_DOW_UIN_FAILURE_SMS', descr='Download e-UIN Failure SMS', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_FAILURE_EMAIL', descr='Failure in Locking of Auth Types Email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_FAILURE_EMAIL_SUB', descr='Failure in Locking of Auth Types Email Subject', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_LOCK_AUTH_FAILURE_SMS', descr='Failure in Locking of Auth Types SMS', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_FAILURE_EMAIL', descr='Reprint Request Failure Email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_FAILURE_EMAIL_SUB', descr='Reprint Request Failure Email Subject', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_RPR_FAILURE_SMS', descr='Reprint Request Failure SMS', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_FAILURE_EMAIL', descr='Failure in Unlocking of Auth Types Email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_FAILURE_EMAIL_SUB', descr='Failure in Unlocking of Auth Types Email Subject', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UNLOCK_AUTH_FAILURE_SMS', descr='Failure in Unlocking of Auth Types SMS', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_FAILURE_EMAIL', descr='VID Generation Failure Email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_FAILURE_EMAIL_SUB', descr='VID Generation Failure Email Subject', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_GEN_FAILURE_SMS', descr='VID Generation Failure SMS', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_FAILURE_EMAIL', descr='VID Revocation Failure Email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_FAILURE_EMAIL_SUB', descr='VID Revocation Failure Email Subject', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_VIN_REV_FAILURE_SMS', descr='VID Revocation Failure SMS', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_SUCCESS_EMAIL', descr='UIN Update Request Placed Successfully Email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_SUCCESS_EMAIL_SUB', descr='UIN Update Request Placed Successfully Email Subject', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_SUCCESS_SMS', descr='UIN Update Request Placed Successfully SMS', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_FAILURE_EMAIL', descr='UIN Update Request Failed Email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_FAILURE_EMAIL_SUB', descr='UIN Update Request Failed Email Subject', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_UIN_UPDATE_FAILURE_SMS', descr='UIN Update Request Failed SMS', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_SUCCESS_SMS', descr='Credential Issuance Success SMS', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_SUCCESS_EMAIL', descr='Credential Issuance Success EMAIL', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_SUCCESS_EMAIL_SUB', descr='Credential Issuance Success EMAIL Subject', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_STATUS_SMS', descr='Credential Issuance Status Check SMS', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_STATUS_EMAIL', descr='Credential Issuance Status Check EMAIL', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_STATUS_EMAIL_SUB', descr='Credential Issuance Status Check EMAIL Subject', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_CANCEL_SUCCESS_SMS', descr='Credential Request Cancel Success SMS', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_CANCEL_SUCCESS_EMAIL', descr='Credential Request Cancel Success EMAIL', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_CANCEL_SUCCESS_EMAIL_SUB', descr='Credential Request Cancel Success EMAIL Subject', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_FAILURE_SMS', descr='Credential Issuance Failure SMS', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_FAILURE_EMAIL', descr='Credential Issuance Failure EMAIL', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='RS_CRE_REQ_FAILURE_EMAIL_SUB', descr='Credential Issuance Failure EMAIL Subject', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-request-received-email-subject', descr='Request received email subject to customize and download my card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-request-received-email-subject', descr='Request received email subject to customize and download my card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-request-received-email-subject', descr='Request received email subject to customize and download my card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-request-received-email-subject', descr='Request received email subject to customize and download my card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-request-received-email-subject', descr='Request received email subject to customize and download my card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-request-received-email-subject', descr='Request received email subject to customize and download my card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success-email-subject', descr='Success email subject to customize and download my card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success-email-subject', descr='Success email subject to customize and download my card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success-email-subject', descr='Success email subject to customize and download my card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success-email-subject', descr='Success email subject to customize and download my card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success-email-subject', descr='Success email subject to customize and download my card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success-email-subject', descr='Success email subject to customize and download my card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-failure-email-subject', descr='Failure email subject to customize and download my card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-failure-email-subject', descr='Failure email subject to customize and download my card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-failure-email-subject', descr='Failure email subject to customize and download my card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-failure-email-subject', descr='Failure email subject to customize and download my card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-failure-email-subject', descr='Failure email subject to customize and download my card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-failure-email-subject', descr='Failure email subject to customize and download my card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-request-received-email-content', descr='Request received email to customize and download my card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success-email-content', descr='Success email to customize and download my card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-failure-email-content', descr='Failure email to customize and download my card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-request-received-email-content', descr='Request received email to customize and download my card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success-email-content', descr='Success email to customize and download my card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-failure-email-content', descr='Failure email to customize and download my card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-request-received-email-content', descr='Request received email to customize and download my card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success-email-content', descr='Success email to customize and download my card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-failure-email-content', descr='Failure email to customize and download my card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-request-received-email-content', descr='Request received email to customize and download my card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success-email-content', descr='Success email to customize and download my card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-failure-email-content', descr='Failure email to customize and download my card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-request-received-email-content', descr='Request received email to customize and download my card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success-email-content', descr='Success email to customize and download my card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-failure-email-content', descr='Failure email to customize and download my card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-request-received-email-content', descr='Request received email to customize and download my card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success-email-content', descr='Success email to customize and download my card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-failure-email-content', descr='Failure email to customize and download my card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-request-received-email-subject', descr='Request received email subject to order a physical card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-request-received-email-subject', descr='Request received email subject to order a physical card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-request-received-email-subject', descr='Request received email subject to order a physical card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-request-received-email-subject', descr='Request received email subject to order a physical card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-request-received-email-subject', descr='Request received email subject to order a physical card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-request-received-email-subject', descr='Request received email subject to order a physical card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success-email-subject', descr='Success email subject to order a physical card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success-email-subject', descr='Success email subject to order a physical card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success-email-subject', descr='Success email subject to order a physical card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success-email-subject', descr='Success email subject to order a physical card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success-email-subject', descr='Success email subject to order a physical card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success-email-subject', descr='Success email subject to order a physical card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-failure-email-subject', descr='Failure email subject to order a physical card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-failure-email-subject', descr='Failure email subject to order a physical card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-failure-email-subject', descr='Failure email subject to order a physical card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-failure-email-subject', descr='Failure email subject to order a physical card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-failure-email-subject', descr='Failure email subject to order a physical card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-failure-email-subject', descr='Failure email subject to order a physical card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-request-received-email-content', descr='Request received email to order a physical card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success-email-content', descr='Success email to order a physical card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-failure-email-content', descr='Failure email to order a physical card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-request-received-email-content', descr='Request received email to order a physical card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success-email-content', descr='Success email to order a physical card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-failure-email-content', descr='Failure email to order a physical card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-request-received-email-content', descr='Request received email to order a physical card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success-email-content', descr='Success email to order a physical card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-failure-email-content', descr='Failure email to order a physical card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-request-received-email-content', descr='Request received email to order a physical card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success-email-content', descr='Success email to order a physical card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-failure-email-content', descr='Failure email to order a physical card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-request-received-email-content', descr='Request received email to order a physical card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success-email-content', descr='Success email to order a physical card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-failure-email-content', descr='Failure email to order a physical card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-request-received-email-content', descr='Request received email to order a physical card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success-email-content', descr='Success email to order a physical card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-failure-email-content', descr='Failure email to order a physical card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-request-received-email-subject', descr='Request received email subject to share my credential with a partner', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-request-received-email-subject', descr='Request received email subject to share my credential with a partner', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-request-received-email-subject', descr='Request received email subject to share my credential with a partner', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-request-received-email-subject', descr='Request received email subject to share my credential with a partner', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-request-received-email-subject', descr='Request received email subject to share my credential with a partner', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-request-received-email-subject', descr='Request received email subject to share my credential with a partner', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success-email-subject', descr='Success email subject to share my credential with a partner', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success-email-subject', descr='Success email subject to share my credential with a partner', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success-email-subject', descr='Success email subject to share my credential with a partner', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success-email-subject', descr='Success email subject to share my credential with a partner', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success-email-subject', descr='Success email subject to share my credential with a partner', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success-email-subject', descr='Success email subject to share my credential with a partner', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-failure-email-subject', descr='Failure email subject to share my credential with a partner', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-failure-email-subject', descr='Failure email subject to share my credential with a partner', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-failure-email-subject', descr='Failure email subject to share my credential with a partner', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-failure-email-subject', descr='Failure email subject to share my credential with a partner', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-failure-email-subject', descr='Failure email subject to share my credential with a partner', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-failure-email-subject', descr='Failure email subject to share my credential with a partner', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-request-received-email-content', descr='Request received email to share my credential with a partner', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success-email-content', descr='Success email to share my credential with a partner', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-failure-email-content', descr='Failure email to share my credential with a partner', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-request-received-email-content', descr='Request received email to share my credential with a partner', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success-email-content', descr='Success email to share my credential with a partner', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-failure-email-content', descr='Failure email to share my credential with a partner', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-request-received-email-content', descr='Request received email to share my credential with a partner', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success-email-content', descr='Success email to share my credential with a partner', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-failure-email-content', descr='Failure email to share my credential with a partner', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-request-received-email-content', descr='Request received email to share my credential with a partner', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success-email-content', descr='Success email to share my credential with a partner', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-failure-email-content', descr='Failure email to share my credential with a partner', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-request-received-email-content', descr='Request received email to share my credential with a partner', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success-email-content', descr='Success email to share my credential with a partner', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-failure-email-content', descr='Failure email to share my credential with a partner', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-request-received-email-content', descr='Request received email to share my credential with a partner', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success-email-content', descr='Success email to share my credential with a partner', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-failure-email-content', descr='Failure email to share my credential with a partner', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-request-received-email-subject', descr='Request received email subject to lock/unlock authentication', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-request-received-email-subject', descr='Request received email subject to lock/unlock authentication', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-request-received-email-subject', descr='Request received email subject to lock/unlock authentication', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-request-received-email-subject', descr='Request received email subject to lock/unlock authentication', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-request-received-email-subject', descr='Request received email subject to lock/unlock authentication', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-request-received-email-subject', descr='Request received email subject to lock/unlock authentication', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success-email-subject', descr='Success email subject to lock/unlock authentication', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success-email-subject', descr='Success email subject to lock/unlock authentication', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success-email-subject', descr='Success email subject to lock/unlock authentication', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success-email-subject', descr='Success email subject to lock/unlock authentication', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success-email-subject', descr='Success email subject to lock/unlock authentication', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success-email-subject', descr='Success email subject to lock/unlock authentication', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-failure-email-subject', descr='Failure email subject to lock/unlock authentication', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-failure-email-subject', descr='Failure email subject to lock/unlock authentication', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-failure-email-subject', descr='Failure email subject to lock/unlock authentication', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-failure-email-subject', descr='Failure email subject to lock/unlock authentication', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-failure-email-subject', descr='Failure email subject to lock/unlock authentication', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-failure-email-subject', descr='Failure email subject to lock/unlock authentication', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-request-received-email-content', descr='Request received email to lock/unlock authentication', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success-email-content', descr='Success email to lock/unlock authentication', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-failure-email-content', descr='Failure email to lock/unlock authentication', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-request-received-email-content', descr='Request received email to lock/unlock authentication', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success-email-content', descr='Success email to lock/unlock authentication', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-failure-email-content', descr='Failure email to lock/unlock authentication', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-request-received-email-content', descr='Request received email to lock/unlock authentication', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success-email-content', descr='Success email to lock/unlock authentication', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-failure-email-content', descr='Failure email to lock/unlock authentication', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-request-received-email-content', descr='Request received email to lock/unlock authentication', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success-email-content', descr='Success email to lock/unlock authentication', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-failure-email-content', descr='Failure email to lock/unlock authentication', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-request-received-email-content', descr='Request received email to lock/unlock authentication', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success-email-content', descr='Success email to lock/unlock authentication', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-failure-email-content', descr='Failure email to lock/unlock authentication', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-request-received-email-content', descr='Request received email to lock/unlock authentication', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success-email-content', descr='Success email to lock/unlock authentication', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-failure-email-content', descr='Failure email to lock/unlock authentication', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-request-received-email-subject', descr='Request received email subject to self update demographic data', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-request-received-email-subject', descr='Request received email subject to self update demographic data', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-request-received-email-subject', descr='Request received email subject to self update demographic data', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-request-received-email-subject', descr='Request received email subject to self update demographic data', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-request-received-email-subject', descr='Request received email subject to self update demographic data', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-request-received-email-subject', descr='Request received email subject to self update demographic data', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success-email-subject', descr='Success email subject to self update demographic data', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success-email-subject', descr='Success email subject to self update demographic data', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success-email-subject', descr='Success email subject to self update demographic data', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success-email-subject', descr='Success email subject to self update demographic data', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success-email-subject', descr='Success email subject to self update demographic data', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success-email-subject', descr='Success email subject to self update demographic data', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-failure-email-subject', descr='Failure email subject to self update demographic data', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-failure-email-subject', descr='Failure email subject to self update demographic data', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-failure-email-subject', descr='Failure email subject to self update demographic data', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-failure-email-subject', descr='Failure email subject to self update demographic data', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-failure-email-subject', descr='Failure email subject to self update demographic data', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-failure-email-subject', descr='Failure email subject to self update demographic data', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-request-received-email-content', descr='Request received email to self update demographic data', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success-email-content', descr='Success email to self update demographic data', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-failure-email-content', descr='Failure email to self update demographic data', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-request-received-email-content', descr='Request received email to self update demographic data', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success-email-content', descr='Success email to self update demographic data', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-failure-email-content', descr='Failure email to self update demographic data', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-request-received-email-content', descr='Request received email to self update demographic data', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success-email-content', descr='Success email to self update demographic data', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-failure-email-content', descr='Failure email to self update demographic data', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-request-received-email-content', descr='Request received email to self update demographic data', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success-email-content', descr='Success email to self update demographic data', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-failure-email-content', descr='Failure email to self update demographic data', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-request-received-email-content', descr='Request received email to self update demographic data', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success-email-content', descr='Success email to self update demographic data', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-failure-email-content', descr='Failure email to self update demographic data', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-request-received-email-content', descr='Request received email to self update demographic data', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success-email-content', descr='Success email to self update demographic data', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-failure-email-content', descr='Failure email to self update demographic data', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-request-received-email-subject', descr='Request received email subject to generate or revoke VID', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-request-received-email-subject', descr='Request received email subject to generate or revoke VID', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-request-received-email-subject', descr='Request received email subject to generate or revoke VID', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-request-received-email-subject', descr='Request received email subject to generate or revoke VID', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-request-received-email-subject', descr='Request received email subject to generate or revoke VID', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-request-received-email-subject', descr='Request received email subject to generate or revoke VID', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success-email-subject', descr='Success email subject to generate or revoke VID', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success-email-subject', descr='Success email subject to generate or revoke VID', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success-email-subject', descr='Success email subject to generate or revoke VID', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success-email-subject', descr='Success email subject to generate or revoke VID', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success-email-subject', descr='Success email subject to generate or revoke VID', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success-email-subject', descr='Success email subject to generate or revoke VID', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-failure-email-subject', descr='Failure email subject to generate or revoke VID', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-failure-email-subject', descr='Failure email subject to generate or revoke VID', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-failure-email-subject', descr='Failure email subject to generate or revoke VID', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-failure-email-subject', descr='Failure email subject to generate or revoke VID', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-failure-email-subject', descr='Failure email subject to generate or revoke VID', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-failure-email-subject', descr='Failure email subject to generate or revoke VID', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-request-received-email-content', descr='Request received email to generate or revoke VID', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success-email-content', descr='Success email to generate or revoke VID', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-failure-email-content', descr='Failure email to generate or revoke VID', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-request-received-email-content', descr='Request received email to generate or revoke VID', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success-email-content', descr='Success email to generate or revoke VID', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-failure-email-content', descr='Failure email to generate or revoke VID', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-request-received-email-content', descr='Request received email to generate or revoke VID', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success-email-content', descr='Success email to generate or revoke VID', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-failure-email-content', descr='Failure email to generate or revoke VID', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-request-received-email-content', descr='Request received email to generate or revoke VID', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success-email-content', descr='Success email to generate or revoke VID', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-failure-email-content', descr='Failure email to generate or revoke VID', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-request-received-email-content', descr='Request received email to generate or revoke VID', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success-email-content', descr='Success email to generate or revoke VID', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-failure-email-content', descr='Failure email to generate or revoke VID', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-request-received-email-content', descr='Request received email to generate or revoke VID', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success-email-content', descr='Success email to generate or revoke VID', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-failure-email-content', descr='Failure email to generate or revoke VID', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-request-received-email-subject', descr='Request received email subject to get my UIN card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-request-received-email-subject', descr='Request received email subject to get my UIN card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-request-received-email-subject', descr='Request received email subject to get my UIN card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-request-received-email-subject', descr='Request received email subject to get my UIN card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-request-received-email-subject', descr='Request received email subject to get my UIN card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-request-received-email-subject', descr='Request received email subject to get my UIN card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success-email-subject', descr='Success email subject to get my UIN card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success-email-subject', descr='Success email subject to get my UIN card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success-email-subject', descr='Success email subject to get my UIN card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success-email-subject', descr='Success email subject to get my UIN card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success-email-subject', descr='Success email subject to get my UIN card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success-email-subject', descr='Success email subject to get my UIN card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-failure-email-subject', descr='Failure email subject to get my UIN card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-failure-email-subject', descr='Failure email subject to get my UIN card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-failure-email-subject', descr='Failure email subject to get my UIN card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-failure-email-subject', descr='Failure email subject to get my UIN card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-failure-email-subject', descr='Failure email subject to get my UIN card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-failure-email-subject', descr='Failure email subject to get my UIN card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-request-received-email-content', descr='Request received email to get my UIN card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success-email-content', descr='Success email to get my UIN card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-failure-email-content', descr='Failure email to get my UIN card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-request-received-email-content', descr='Request received email to get my UIN card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success-email-content', descr='Success email to get my UIN card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-failure-email-content', descr='Failure email to get my UIN card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-request-received-email-content', descr='Request received email to get my UIN card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success-email-content', descr='Success email to get my UIN card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-failure-email-content', descr='Failure email to get my UIN card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-request-received-email-content', descr='Request received email to get my UIN card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success-email-content', descr='Success email to get my UIN card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-failure-email-content', descr='Failure email to get my UIN card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-request-received-email-content', descr='Request received email to get my UIN card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success-email-content', descr='Success email to get my UIN card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-failure-email-content', descr='Failure email to get my UIN card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-request-received-email-content', descr='Request received email to get my UIN card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success-email-content', descr='Success email to get my UIN card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-failure-email-content', descr='Failure email to get my UIN card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-request-received-email-subject', descr='Request received email subject to verify my phone and email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-request-received-email-subject', descr='Request received email subject to verify my phone and email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-request-received-email-subject', descr='Request received email subject to verify my phone and email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-request-received-email-subject', descr='Request received email subject to verify my phone and email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-request-received-email-subject', descr='Request received email subject to verify my phone and email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-request-received-email-subject', descr='Request received email subject to verify my phone and email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success-email-subject', descr='Success email subject to verify my phone and email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success-email-subject', descr='Success email subject to verify my phone and email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success-email-subject', descr='Success email subject to verify my phone and email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success-email-subject', descr='Success email subject to verify my phone and email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success-email-subject', descr='Success email subject to verify my phone and email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success-email-subject', descr='Success email subject to verify my phone and email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-failure-email-subject', descr='Failure email subject to verify my phone and email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='receive-otp', descr='Receive OTP', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-failure-email-subject', descr='Failure email subject to verify my phone and email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-failure-email-subject', descr='Failure email subject to verify my phone and email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-failure-email-subject', descr='Failure email subject to verify my phone and email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-failure-email-subject', descr='Failure email subject to verify my phone and email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-failure-email-subject', descr='Failure email subject to verify my phone and email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-request-received-email-content', descr='Request received email to verify my phone and email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success-email-content', descr='Success email to verify my phone and email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-failure-email-content', descr='Failure email to verify my phone and email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-request-received-email-content', descr='Request received email to verify my phone and email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success-email-content', descr='Success email to verify my phone and email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-failure-email-content', descr='Failure email to verify my phone and email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-request-received-email-content', descr='Request received email to verify my phone and email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success-email-content', descr='Success email to verify my phone and email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-failure-email-content', descr='Failure email to verify my phone and email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-request-received-email-content', descr='Request received email to verify my phone and email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success-email-content', descr='Success email to verify my phone and email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-failure-email-content', descr='Failure email to verify my phone and email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-request-received-email-content', descr='Request received email to verify my phone and email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success-email-content', descr='Success email to verify my phone and email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-failure-email-content', descr='Failure email to verify my phone and email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-request-received-email-content', descr='Request received email to verify my phone and email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success-email-content', descr='Success email to verify my phone and email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-failure-email-content', descr='Failure email to verify my phone and email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='acknowledgement-share-cred-with-partner', descr='Acknowledgement for share credential with a partner', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-request-received_SMS', descr='Request received sms to customize and download my card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success_SMS', descr='Success sms to customize and download my card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-failure_SMS', descr='Failure sms to customize and download my card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-request-received_SMS', descr='Request received sms to customize and download my card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success_SMS', descr='Success sms to customize and download my card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-failure_SMS', descr='Failure sms to customize and download my card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-request-received_SMS', descr='Request received sms to customize and download my card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success_SMS', descr='Success sms to customize and download my card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-failure_SMS', descr='Failure sms to customize and download my card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-request-received_SMS', descr='Request received sms to customize and download my card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success_SMS', descr='Success sms to customize and download my card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-failure_SMS', descr='Failure sms to customize and download my card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-request-received_SMS', descr='Request received sms to customize and download my card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success_SMS', descr='Success sms to customize and download my card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-failure_SMS', descr='Failure sms to customize and download my card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-request-received_SMS', descr='Request received sms to customize and download my card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success_SMS', descr='Success sms to customize and download my card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-failure_SMS', descr='Failure sms to customize and download my card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-request-received_SMS', descr='Request received sms to order a physical card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success_SMS', descr='Success sms to order a physical card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-failure_SMS', descr='Failure sms to order a physical card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-request-received_SMS', descr='Request received sms to order a physical card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success_SMS', descr='Success sms to order a physical card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-failure_SMS', descr='Failure sms to order a physical card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-request-received_SMS', descr='Request received sms to order a physical card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success_SMS', descr='Success sms to order a physical card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-failure_SMS', descr='Failure sms to order a physical card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-request-received_SMS', descr='Request received sms to order a physical card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success_SMS', descr='Success sms to order a physical card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-failure_SMS', descr='Failure sms to order a physical card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-request-received_SMS', descr='Request received sms to order a physical card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success_SMS', descr='Success sms to order a physical card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-failure_SMS', descr='Failure sms to order a physical card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-request-received_SMS', descr='Request received sms to order a physical card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success_SMS', descr='Success sms to order a physical card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-failure_SMS', descr='Failure sms to order a physical card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-request-received_SMS', descr='Request received sms to share my credential with a partner', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success_SMS', descr='Success sms to share my credential with a partner', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-failure_SMS', descr='Failure sms to share my credential with a partner', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-request-received_SMS', descr='Request received sms to share my credential with a partner', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success_SMS', descr='Success sms to share my credential with a partner', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-failure_SMS', descr='Failure sms to share my credential with a partner', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-request-received_SMS', descr='Request received sms to share my credential with a partner', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success_SMS', descr='Success sms to share my credential with a partner', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-failure_SMS', descr='Failure sms to share my credential with a partner', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-request-received_SMS', descr='Request received sms to share my credential with a partner', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success_SMS', descr='Success sms to share my credential with a partner', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-failure_SMS', descr='Failure sms to share my credential with a partner', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-request-received_SMS', descr='Request received sms to share my credential with a partner', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success_SMS', descr='Success sms to share my credential with a partner', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-failure_SMS', descr='Failure sms to share my credential with a partner', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-request-received_SMS', descr='Request received sms to share my credential with a partner', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success_SMS', descr='Success sms to share my credential with a partner', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-failure_SMS', descr='Failure sms to share my credential with a partner', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-request-received_SMS', descr='Request received sms to lock/unlock authentication', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success_SMS', descr='Success sms to lock/unlock authentication', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-failure_SMS', descr='Failure sms to lock/unlock authentication', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-request-received_SMS', descr='Request received sms to lock/unlock authentication', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success_SMS', descr='Success sms to lock/unlock authentication', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-failure_SMS', descr='Failure sms to lock/unlock authentication', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-request-received_SMS', descr='Request received sms to lock/unlock authentication', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success_SMS', descr='Success sms to lock/unlock authentication', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-failure_SMS', descr='Failure sms to lock/unlock authentication', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-request-received_SMS', descr='Request received sms to lock/unlock authentication', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success_SMS', descr='Success sms to lock/unlock authentication', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-failure_SMS', descr='Failure sms to lock/unlock authentication', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-request-received_SMS', descr='Request received sms to lock/unlock authentication', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success_SMS', descr='Success sms to lock/unlock authentication', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-failure_SMS', descr='Failure sms to lock/unlock authentication', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-request-received_SMS', descr='Request received sms to lock/unlock authentication', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success_SMS', descr='Success sms to lock/unlock authentication', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-failure_SMS', descr='Failure sms to lock/unlock authentication', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-request-received_SMS', descr='Request received sms to self update demographic data', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success_SMS', descr='Success sms to self update demographic data', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-failure_SMS', descr='Failure sms to self update demographic data', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-request-received_SMS', descr='Request received sms to self update demographic data', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success_SMS', descr='Success sms to self update demographic data', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-failure_SMS', descr='Failure sms to self update demographic data', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-request-received_SMS', descr='Request received sms to self update demographic data', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success_SMS', descr='Success sms to self update demographic data', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-failure_SMS', descr='Failure sms to self update demographic data', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-request-received_SMS', descr='Request received sms to self update demographic data', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success_SMS', descr='Success sms to self update demographic data', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-failure_SMS', descr='Failure sms to self update demographic data', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-request-received_SMS', descr='Request received sms to self update demographic data', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success_SMS', descr='Success sms to self update demographic data', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-failure_SMS', descr='Failure sms to self update demographic data', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-request-received_SMS', descr='Request received sms to self update demographic data', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success_SMS', descr='Success sms to self update demographic data', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-failure_SMS', descr='Failure sms to self update demographic data', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-request-received_SMS', descr='Request received sms to generate or revoke VID', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success_SMS', descr='Success sms to generate or revoke VID', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-failure_SMS', descr='Failure sms to generate or revoke VID', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-request-received_SMS', descr='Request received sms to generate or revoke VID', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success_SMS', descr='Success sms to generate or revoke VID', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-failure_SMS', descr='Failure sms to generate or revoke VID', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-request-received_SMS', descr='Request received sms to generate or revoke VID', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success_SMS', descr='Success sms to generate or revoke VID', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-failure_SMS', descr='Failure sms to generate or revoke VID', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-request-received_SMS', descr='Request received sms to generate or revoke VID', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success_SMS', descr='Success sms to generate or revoke VID', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-failure_SMS', descr='Failure sms to generate or revoke VID', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-request-received_SMS', descr='Request received sms to generate or revoke VID', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success_SMS', descr='Success sms to generate or revoke VID', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-failure_SMS', descr='Failure sms to generate or revoke VID', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-request-received_SMS', descr='Request received sms to generate or revoke VID', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success_SMS', descr='Success sms to generate or revoke VID', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-failure_SMS', descr='Failure sms to generate or revoke VID', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-request-received_SMS', descr='Request received sms to get my UIN card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success_SMS', descr='Success sms to get my UIN card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-failure_SMS', descr='Failure sms to get my UIN card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-request-received_SMS', descr='Request received sms to get my UIN card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success_SMS', descr='Success sms to get my UIN card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-failure_SMS', descr='Failure sms to get my UIN card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-request-received_SMS', descr='Request received sms to get my UIN card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success_SMS', descr='Success sms to get my UIN card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-failure_SMS', descr='Failure sms to get my UIN card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-request-received_SMS', descr='Request received sms to get my UIN card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success_SMS', descr='Success sms to get my UIN card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-failure_SMS', descr='Failure sms to get my UIN card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-request-received_SMS', descr='Request received sms to get my UIN card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success_SMS', descr='Success sms to get my UIN card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-failure_SMS', descr='Failure sms to get my UIN card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-request-received_SMS', descr='Request received sms to get my UIN card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success_SMS', descr='Success sms to get my UIN card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-failure_SMS', descr='Failure sms to get my UIN card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-request-received_SMS', descr='Request received sms to verify my phone and email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success_SMS', descr='Success sms to verify my phone and email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-failure_SMS', descr='Failure sms to verify my phone and email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-request-received_SMS', descr='Request received sms to verify my phone and email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success_SMS', descr='Success sms to verify my phone and email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-failure_SMS', descr='Failure sms to verify my phone and email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-request-received_SMS', descr='Request received sms to verify my phone and email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success_SMS', descr='Success sms to verify my phone and email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-failure_SMS', descr='Failure sms to verify my phone and email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-request-received_SMS', descr='Request received sms to verify my phone and email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success_SMS', descr='Success sms to verify my phone and email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-failure_SMS', descr='Failure sms to verify my phone and email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-request-received_SMS', descr='Request received sms to verify my phone and email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success_SMS', descr='Success sms to verify my phone and email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='receive-otp', descr='Receive OTP', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-failure_SMS', descr='Failure sms to verify my phone and email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-request-received_SMS', descr='Request received sms to verify my phone and email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success_SMS', descr='Success sms to verify my phone and email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-failure_SMS', descr='Failure sms to verify my phone and email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='receive-otp', descr='Receive OTP', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-positive-purpose', descr='Positive purpose to customize and download my card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-negative-purpose', descr='Negative purpose to customize and download my card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-positive-purpose', descr='Positive purpose to customize and download my card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-negative-purpose', descr='Negative purpose to customize and download my card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-positive-purpose', descr='Positive purpose to customize and download my card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-negative-purpose', descr='Negative purpose to customize and download my card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-positive-purpose', descr='Positive purpose to customize and download my card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-negative-purpose', descr='Negative purpose to customize and download my card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-positive-purpose', descr='Positive purpose to customize and download my card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='receive-otp', descr='Receive OTP', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-negative-purpose', descr='Negative purpose to customize and download my card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-positive-purpose', descr='Positive purpose to customize and download my card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-negative-purpose', descr='Negative purpose to customize and download my card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-positive purpose', descr='Positive purpose to order a physical card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-negative purpose', descr='Negative purpose to order a physical card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-positive purpose', descr='Positive purpose to order a physical card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-negative purpose', descr='Negative purpose to order a physical card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-positive purpose', descr='Positive purpose to order a physical card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-negative purpose', descr='Negative purpose to order a physical card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-positive purpose', descr='Positive purpose to order a physical card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-negative purpose', descr='Negative purpose to order a physical card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-positive purpose', descr='Positive purpose to order a physical card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-negative purpose', descr='Negative purpose to order a physical card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-positive purpose', descr='Positive purpose to order a physical card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-negative purpose', descr='Negative purpose to order a physical card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-positive-purpose', descr='Positive purpose to share my credential with a partner', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-negative-purpose', descr='Negative purpose to share my credential with a partner', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-positive-purpose', descr='Positive purpose to share my credential with a partner', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-negative-purpose', descr='Negative purpose to share my credential with a partner', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-positive-purpose', descr='Positive purpose to share my credential with a partner', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-negative-purpose', descr='Negative purpose to share my credential with a partner', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-positive-purpose', descr='Positive purpose to share my credential with a partner', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-negative-purpose', descr='Negative purpose to share my credential with a partner', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-positive-purpose', descr='Positive purpose to share my credential with a partner', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-negative-purpose', descr='Negative purpose to share my credential with a partner', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-positive-purpose', descr='Positive purpose to share my credential with a partner', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-negative-purpose', descr='Negative purpose to share my credential with a partner', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-positive-purpose', descr='Positive purpose to lock/unlock various authentication types', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-negative-purpose', descr='Negative purpose to lock/unlock various authentication types', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-positive-purpose', descr='Positive purpose to lock/unlock various authentication types', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-negative-purpose', descr='Negative purpose to lock/unlock various authentication types', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-positive-purpose', descr='Positive purpose to lock/unlock various authentication types', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-negative-purpose', descr='Negative purpose to lock/unlock various authentication types', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-positive-purpose', descr='Positive purpose to lock/unlock various authentication types', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-negative-purpose', descr='Negative purpose to lock/unlock various authentication types', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-positive-purpose', descr='Positive purpose to lock/unlock various authentication types', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-negative-purpose', descr='Negative purpose to lock/unlock various authentication types', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-positive-purpose', descr='Positive purpose to lock/unlock various authentication types', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-negative-purpose', descr='Negative purpose to lock/unlock various authentication types', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-positive-purpose', descr='Positive Purpose to self update demographic data', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-negative-purpose', descr='Negative Purpose to self update demographic data', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-positive-purpose', descr='Positive Purpose to self update demographic data', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-negative-purpose', descr='Negative Purpose to self update demographic data', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-positive-purpose', descr='Positive Purpose to self update demographic data', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-negative-purpose', descr='Negative Purpose to self update demographic data', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-positive-purpose', descr='Positive Purpose to self update demographic data', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-negative-purpose', descr='Negative Purpose to self update demographic data', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-positive-purpose', descr='Positive Purpose to self update demographic data', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-negative-purpose', descr='Negative Purpose to self update demographic data', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-positive-purpose', descr='Positive Purpose to self update demographic data', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-negative-purpose', descr='Negative Purpose to self update demographic data', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-positive-purpose', descr='Positive Purpose  to generate or revoke VIDs', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-negative-purpose', descr='Negative Purpose to generate or revoke VIDs', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-positive-purpose', descr='Positive Purpose  to generate or revoke VIDs', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-negative-purpose', descr='Negative Purpose to generate or revoke VIDs', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-positive-purpose', descr='Positive Purpose  to generate or revoke VIDs', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-negative-purpose', descr='Negative Purpose to generate or revoke VIDs', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-positive-purpose', descr='Positive Purpose  to generate or revoke VIDs', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-negative-purpose', descr='Negative Purpose to generate or revoke VIDs', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-positive-purpose', descr='Positive Purpose  to generate or revoke VIDs', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-negative-purpose', descr='Negative Purpose to generate or revoke VIDs', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-positive-purpose', descr='Positive Purpose  to generate or revoke VIDs', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-negative-purpose', descr='Negative Purpose to generate or revoke VIDs', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-positive-purpose', descr='Positive purpose to get my UIN card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-negative-purpose', descr='Negative purpose to get my UIN card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-positive-purpose', descr='Positive purpose to get my UIN card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-negative-purpose', descr='Negative purpose to get my UIN card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-positive-purpose', descr='Positive purpose to get my UIN card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-negative-purpose', descr='Negative purpose to get my UIN card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-positive-purpose', descr='Positive purpose to get my UIN card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-negative-purpose', descr='Negative purpose to get my UIN card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-positive-purpose', descr='Positive purpose to get my UIN card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-negative-purpose', descr='Negative purpose to get my UIN card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-positive-purpose', descr='Positive purpose to get my UIN card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-negative-purpose', descr='Negative purpose to get my UIN card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-positive-purpose', descr='Positive purpose to verify my phone number and email ID', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-negative-purpose', descr='Negative purpose to verify my phone number and email ID', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-positive-purpose', descr='Positive purpose to verify my phone number and email ID', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-negative-purpose', descr='Negative purpose to verify my phone number and email ID', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-positive-purpose', descr='Positive purpose to verify my phone number and email ID', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-negative-purpose', descr='Negative purpose to verify my phone number and email ID', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-positive-purpose', descr='Positive purpose to verify my phone number and email ID', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-negative-purpose', descr='Negative purpose to verify my phone number and email ID', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-positive-purpose', descr='Positive purpose to verify my phone number and email ID', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-negative-purpose', descr='Negative purpose to verify my phone number and email ID', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-positive-purpose', descr='Positive purpose to verify my phone number and email ID', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-negative-purpose', descr='Negative purpose to verify my phone number and email ID', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success-summary', descr='Success summary to customize and download my card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success-summary', descr='Success summary to order a physical card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success-summary', descr='Success summary to share my credential with a partner', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success-summary', descr='Success summary to lock/unlock various authentication types', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success-summary', descr='Success summary to self update demographic data', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success-summary', descr='Success summary to generate or revoke VIDs', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success-summary', descr='Success summary to get my UIN card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success-summary', descr='Success summary to verify my phone and email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success-summary', descr='Success summary to customize and download my card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success-summary', descr='Success summary to order a physical card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success-summary', descr='Success summary to share my credential with a partner', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success-summary', descr='Success summary to lock/unlock various authentication types', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success-summary', descr='Success summary to self update demographic data', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success-summary', descr='Success summary to generate or revoke VIDs', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success-summary', descr='Success summary to get my UIN card', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success-summary', descr='Success summary to verify my phone and email', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success-summary', descr='Success summary to customize and download my card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success-summary', descr='Success summary to order a physical card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success-summary', descr='Success summary to share my credential with a partner', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='receive-otp', descr='Receive OTP', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success-summary', descr='Success summary to lock/unlock various authentication types', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success-summary', descr='Success summary to self update demographic data', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success-summary', descr='Success summary to generate or revoke VIDs', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success-summary', descr='Success summary to get my UIN card', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success-summary', descr='Success summary to verify my phone and email', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success-summary', descr='Success summary to customize and download my card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success-summary', descr='Success summary to order a physical card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success-summary', descr='Success summary to share my credential with a partner', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success-summary', descr='Success summary to lock/unlock various authentication types', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success-summary', descr='Success summary to self update demographic data', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success-summary', descr='Success summary to generate or revoke VIDs', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success-summary', descr='Success summary to get my UIN card', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success-summary', descr='Success summary to verify my phone and email', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success-summary', descr='Success summary to customize and download my card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success-summary', descr='Success summary to order a physical card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success-summary', descr='Success summary to share my credential with a partner', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success-summary', descr='Success summary to lock/unlock various authentication types', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success-summary', descr='Success summary to self update demographic data', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success-summary', descr='Success summary to generate or revoke VIDs', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success-summary', descr='Success summary to get my UIN card', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success-summary', descr='Success summary to verify my phone and email', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='cust-and-down-my-card-success-summary', descr='Success summary to customize and download my card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='order-a-physical-card-success-summary', descr='Success summary to order a physical card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='share-cred-with-partner-success-summary', descr='Success summary to share my credential with a partner', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='lock-unlock-auth-success-summary', descr='Success summary to lock/unlock various authentication types', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='update-demo-data-success-summary', descr='Success summary to self update demographic data', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='gen-or-revoke-vid-success-summary', descr='Success summary to generate or revoke VIDs', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='get-my-uin-card-success-summary', descr='Success summary to get my UIN card', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='verify-my-phone-email-success-summary', descr='Success summary to verify my phone and email', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='supporting-docs-list', descr='List of supporting documents', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='acknowledgement-manage-my-vid', descr='Acknowledgement for manage my vid', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='acknowledgement-order-a-physical-card', descr='Acknowledgement for Order a physical card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='acknowledgement-download-a-personalized-card', descr='Acknowledgement for Download a personalized card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='acknowledgement-update-demographic-data', descr='Acknowledgement for Update demographic data', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='acknowledgement-verify-email-id-or-phone-number', descr='Acknowledgement for verify email id or phone number', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='acknowledgement-secure-my-id', descr='Acknowledgement for Secure my Id', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='registration-centers-list', descr='List of registration centers', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='receive-otp-mail-subject', descr='Receiving OTP Mail Subject', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='receive-otp-mail-subject', descr='Receiving OTP Mail Subject', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='receive-otp-mail-subject', descr='Receiving OTP Mail Subject', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='receive-otp-mail-subject', descr='Receiving OTP Mail Subject', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='receive-otp-mail-subject', descr='Receiving OTP Mail Subject', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='receive-otp-mail-subject', descr='Receiving OTP Mail Subject', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='receive-otp-mail-content', descr='Receiving OTP Mail Content', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='receive-otp-mail-content', descr='Receiving OTP Mail Content', lang_code='hin', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='receive-otp-mail-content', descr='Receiving OTP Mail Content', lang_code='tam', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='receive-otp-mail-content', descr='Receiving OTP Mail Content', lang_code='kan', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='receive-otp-mail-content', descr='Receiving OTP Mail Content', lang_code='fra', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='receive-otp-mail-content', descr='Receiving OTP Mail Content', lang_code='ara', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='service-history-type', descr='Acknowledment view service history', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='acknowledgment-authentication-request', descr='Acknowledgment Authentication Request', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='acknowledgment-get-my-id', descr='Acknowledment Get My Id', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='acknowledgment-vid-card-download', descr='Acknowledgment vid card download', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='vid-card-download-positive-purpose', descr='Vid Card Download Positive Purpose', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='vid-card-download-negative-purpose', descr='Vid Card Download Failure Purpose', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='vid-card-download-positive-summary', descr='Vid Card Download Positive Summary', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='mosip.phone.template.property', descr='Phone', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='mosip.email.template.property', descr='Email', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='vid-card-type', descr='Vid Card Type', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='vid-card-download-request-received-email-subject', descr='Request received email subject to download my VID card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='vid-card-download-success-email-subject', descr='Success email subject to download my VID card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='vid-card-download-failure-email-subject', descr='Failure email subject to download my VID card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='vid-card-download-request-received-email-content', descr='Request received email to download my VID card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='vid-card-download-success-email-content', descr='Success email to download my VID card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='vid-card-download-failure-email-content', descr='Failure email to download my VID card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='vid-card-download-request-received_SMS', descr='Request received sms to download my VID card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='vid-card-download-success_SMS', descr='Success sms to download my VID card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='vid-card-download-failure_SMS', descr='Failure sms to download my VID card', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='mosip.otp-email.template.property', descr='Email otp', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='mosip.otp-phone.template.property', descr='Phone otp', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='mosip.demo.template.property', descr='Demographic', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='mosip.bio-finger.template.property', descr='Finger bio', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='mosip.bio-iris.template.property', descr='Iris bio', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='mosip.bio-face.template.property', descr='Face bio', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='mosip.unlocked.template.property', descr='Unlocked status', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
UPDATE master.template_type
SET code='mosip.locked.template.property', descr='Locked status', lang_code='eng', is_active=true, cr_by='admin', cr_dtimes='2024-09-03 08:41:51.844', upd_by=NULL, upd_dtimes=NULL, is_deleted=false, del_dtimes=NULL;
DELETE FROM master."template"
WHERE id='3515' AND lang_code='ara';
DELETE FROM master."template"
WHERE id='3514' AND lang_code='fra';
DELETE FROM master."template"
WHERE id='3513' AND lang_code='hin';
DELETE FROM master."template"
WHERE id='3512' AND lang_code='tam';
DELETE FROM master."template"
WHERE id='3511' AND lang_code='kan';
DELETE FROM master."template"
WHERE id='3510' AND lang_code='spa';
DELETE FROM master."template"
WHERE id='3509' AND lang_code='eng';
DELETE FROM master."template"
WHERE id='3508' AND lang_code='ara';
DELETE FROM master."template"
WHERE id='3507' AND lang_code='fra';
DELETE FROM master."template"
WHERE id='3506' AND lang_code='hin';
DELETE FROM master."template"
WHERE id='3505' AND lang_code='tam';
DELETE FROM master."template"
WHERE id='3504' AND lang_code='kan';
DELETE FROM master."template"
WHERE id='3503' AND lang_code='spa';
DELETE FROM master."template"
WHERE id='3502' AND lang_code='eng';
DELETE FROM master."template"
WHERE id='3501' AND lang_code='ara';
DELETE FROM master."template"
WHERE id='3500' AND lang_code='fra';
DELETE FROM master."template"
WHERE id='3499' AND lang_code='hin';
DELETE FROM master."template"
WHERE id='3498' AND lang_code='tam';
DELETE FROM master."template"
WHERE id='3497' AND lang_code='kan';
DELETE FROM master."template"
WHERE id='3496' AND lang_code='spa';
DELETE FROM master."template"
WHERE id='3495' AND lang_code='eng';

DELETE FROM master."template"
WHERE id='1394' AND lang_code='eng';
DELETE FROM master."template"
WHERE id='1394' AND lang_code='fra';
DELETE FROM master."template"
WHERE id='ara' AND lang_code='fra';
DELETE FROM master."template"
WHERE id='ara' AND lang_code='hin';
DELETE FROM master."template"
WHERE id='ara' AND lang_code='kan';
DELETE FROM master."template"
WHERE id='ara' AND lang_code='tam';

DELETE FROM master.template_type
WHERE code='authentication-request-positive-purpose' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-purpose' AND lang_code='spa';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-purpose' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-purpose' AND lang_code='tam';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-purpose' AND lang_code='hin';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-purpose' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-purpose' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='authentication-request-negative-purpose' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='authentication-request-negative-purpose' AND lang_code='spa';
DELETE FROM master.template_type
WHERE code='authentication-request-negative-purpose' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='authentication-request-negative-purpose' AND lang_code='tam';
DELETE FROM master.template_type
WHERE code='authentication-request-negative-purpose' AND lang_code='hin';
DELETE FROM master.template_type
WHERE code='authentication-request-negative-purpose' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='authentication-request-negative-purpose' AND lang_code='ara';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-summary' AND lang_code='eng';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-summary' AND lang_code='spa';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-summary' AND lang_code='kan';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-summary' AND lang_code='tam';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-summary' AND lang_code='hin';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-summary' AND lang_code='fra';
DELETE FROM master.template_type
WHERE code='authentication-request-positive-summary' AND lang_code='ara';