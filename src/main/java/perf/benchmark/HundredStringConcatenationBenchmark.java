/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package perf.benchmark;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Warmup(iterations = 3, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Thread)
public class HundredStringConcatenationBenchmark {
    private static final int LIST_LENGTH = 100;
    private List<String> strings = new ArrayList<>(LIST_LENGTH);

    @Setup
    public void setupTest(){
        for(int i = 0; i < LIST_LENGTH; i++) {
            strings.add(UUID.randomUUID().toString());
        }
    }

    @Benchmark
    public void testStringFormat(Blackhole bh) {
        String combined = "";
        for(String s : strings) {
            combined = String.format("%s%s", combined, s);
        }
        bh.consume(combined);
    }

    @Benchmark
    public void testPlus(Blackhole bh) {
        String combined = "";
        for(String s : strings) {
            combined = combined + s;
        }
        bh.consume(combined);
    }

    @Benchmark
    public void testStringBuilder(Blackhole bh) {
        StringBuilder sb = new StringBuilder();
        for(String s : strings) {
            sb.append(s);
        }
        bh.consume(sb.toString());
    }

    @Benchmark
    public void testStringBuffer(Blackhole bh) {
        StringBuffer sb = new StringBuffer();
        for(String s : strings) {
            sb.append(s);
        }
        bh.consume(sb.toString());
    }

    @Benchmark
    public void testStringJoin(Blackhole bh) {
        bh.consume(String.join("", strings));
    }

    @Benchmark
    public void testStringConcat(Blackhole bh) {
        String combined = "";
        for(String s : strings) {
            combined = combined.concat(s);
        }
        bh.consume(combined);
    }

    @Benchmark
    public void testStringUtilsJoin(Blackhole bh) {
        bh.consume(StringUtils.join(strings, ""));
    }

    @Benchmark
    public void testGuavaJoiner(Blackhole bh) {
        bh.consume(Joiner.on("").join(strings));
    }

    @Benchmark
    public void testStreamJoining(Blackhole bh) {
        String combined = strings
                .stream()
                .collect(Collectors.joining());
        bh.consume(combined);
    }
}
