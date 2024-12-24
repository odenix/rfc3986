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
import static org.assertj.core.api.Assertions.assertThat;
import static org.czeal.rfc3986.HostType.REGNAME;
import static java.nio.charset.StandardCharsets.UTF_8;

import org.junit.jupiter.api.Test;


class AuthorityNormalizerTest
{
    @Test
    void normalize()
    {
        var normalized1 = new AuthorityNormalizer().normalize(
            Authority.parse("userinfoABC@EXAMPLE.com:80"), UTF_8, "http");
        assertThat(normalized1.getUserinfo()).isEqualTo("userinfoABC");
        assertThat(normalized1.getHost().getType()).isEqualTo(REGNAME);
        assertThat(normalized1.getHost().getValue()).isEqualTo("example.com");
        assertThat(normalized1.getPort()).isEqualTo(-1);
        assertThat(normalized1.toString()).isEqualTo("userinfoABC@example.com");

        var normalized2 = new AuthorityNormalizer().normalize(
            Authority.parse("userinfoABC@EXAMPLE.com:443"), UTF_8, "https");
        assertThat(normalized2.getUserinfo()).isEqualTo("userinfoABC");
        assertThat(normalized2.getHost().getType()).isEqualTo(REGNAME);
        assertThat(normalized2.getHost().getValue()).isEqualTo("example.com");
        assertThat(normalized2.getPort()).isEqualTo(-1);
        assertThat(normalized2.toString()).isEqualTo("userinfoABC@example.com");

        var normalized3 = new AuthorityNormalizer().normalize(null, UTF_8, "http");
        assertThat(normalized3).isNull();
    }
}
