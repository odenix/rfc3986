/*
 * Copyright (C) 2024 Hideki Ikeda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.czeal.rfc3986;


import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.czeal.rfc3986.HostType.IPV6;
import static org.czeal.rfc3986.HostType.IPVFUTURE;
import static org.czeal.rfc3986.HostType.IPV4;
import static org.czeal.rfc3986.HostType.REGNAME;
import org.junit.jupiter.api.Test;


class HostNormalizerTest
{
    @Test
    void normalize()
    {
        var normalized1 = new HostNormalizer().normalize(new Host(REGNAME, "HOst"), UTF_8);
        assertThat(normalized1.getType()).isEqualTo(REGNAME);
        assertThat(normalized1.getValue()).isEqualTo("host");

        var normalized2 = new HostNormalizer().normalize(new Host(REGNAME, "hos%74"), UTF_8);
        assertThat(normalized2.getType()).isEqualTo(REGNAME);
        assertThat(normalized2.getValue()).isEqualTo("host");

        var normalized3 = new HostNormalizer().normalize(new Host(REGNAME, "1%2E1%2E1%2E1"), UTF_8);
        assertThat(normalized3.getType()).isEqualTo(IPV4);
        assertThat(normalized3.getValue()).isEqualTo("1.1.1.1");

        var normalized4 = new HostNormalizer().normalize(new Host(REGNAME, "[%32%30%30%31:%30%64%62%38:%38%35%61%33:%30%30%30%30:%30%30%30%30:%38%61%32%65:%30%33%37%30:%37%33%33%34]"), UTF_8);
        assertThat(normalized4.getType()).isEqualTo(IPV6);
        assertThat(normalized4.getValue()).isEqualTo("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]");

        var normalized5 = new HostNormalizer().normalize(new Host(REGNAME, "[%76%31.%66%65%38%30::%61+%65%6E%31]"), UTF_8);
        assertThat(normalized5.getType()).isEqualTo(IPVFUTURE);
        assertThat(normalized5.getValue()).isEqualTo("[v1.fe80::a+en1]");

        var normalized6 = new HostNormalizer().normalize(new Host(REGNAME, ""), UTF_8);
        assertThat(normalized6.getType()).isEqualTo(REGNAME);
        assertThat(normalized6.getValue()).isEqualTo("");

        var normalized7 = new HostNormalizer().normalize(new Host(REGNAME, null), UTF_8);
        assertThat(normalized7.getType()).isEqualTo(REGNAME);
        assertThat(normalized7.getValue()).isNull();
    }
}
