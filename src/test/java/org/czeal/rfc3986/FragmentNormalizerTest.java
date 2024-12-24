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
import org.junit.jupiter.api.Test;


class FragmentNormalizerTest
{
    @Test
    void normalize()
    {
        assertThat(new FragmentNormalizer().normalize("fragment", UTF_8)).isEqualTo("fragment");
        assertThat(new FragmentNormalizer().normalize("FRAGMENT", UTF_8)).isEqualTo("FRAGMENT");
        assertThat(new FragmentNormalizer().normalize("fragmen%74", UTF_8)).isEqualTo("fragment");
        assertThat(new FragmentNormalizer().normalize("", UTF_8)).isEqualTo("");
        assertThat(new FragmentNormalizer().normalize(null, UTF_8)).isEqualTo((String)null);
    }
}
