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


import static java. nio. charset.StandardCharsets.UTF_8;
import static org.czeal.rfc3986.TestUtils.assertThrowsIAE;
import static org.czeal.rfc3986.TestUtils.assertThrowsNPE;
import org.junit.jupiter.api.Test;


class SegmentNzNcValidatorTest
{
    @Test
    void validate()
    {
        new SegmentNzNcValidator().validate("abcde12345-._~", UTF_8);
        new SegmentNzNcValidator().validate("()+_", UTF_8);
        new SegmentNzNcValidator().validate("!$&'()*+,;=", UTF_8);
        new SegmentNzNcValidator().validate("@", UTF_8);

        assertThrowsIAE(
            "The path segment value must not be empty.",
            () -> new SegmentNzNcValidator().validate("", UTF_8));

        assertThrowsNPE(
            "The path segment value must not be null.",
            () -> new SegmentNzNcValidator().validate(null, UTF_8));

        assertThrowsIAE(
            "The path segment value \"segment:\" has an invalid character \":\" at the index 7.",
            () -> new SegmentNzNcValidator().validate("segment:", UTF_8));
    }
}
