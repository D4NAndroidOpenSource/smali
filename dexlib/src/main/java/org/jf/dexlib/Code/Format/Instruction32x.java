/*
 * [The "BSD licence"]
 * Copyright (c) 2009 Ben Gruver
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jf.dexlib.Code.Format;

import org.jf.dexlib.DexFile;
import org.jf.dexlib.Code.Instruction;
import org.jf.dexlib.Code.Opcode;
import org.jf.dexlib.Util.NumberUtils;

public class Instruction32x extends Instruction
{
    public static final Instruction.InstructionFactory Factory = new Factory();

    public Instruction32x(Opcode opcode, int regA, int regB) {
        super(opcode);

        if (regA >= 1<<16 ||
            regB >= 1<<16) {
            throw new RuntimeException("The register number must be less than v65536");
        }

        buffer[0] = opcode.value;
        buffer[2] = (byte)regA;
        buffer[3] = (byte)(regA >> 8);
        buffer[4] = (byte)regB;
        buffer[5] = (byte)(regB >> 8);
    }

    private Instruction32x(Opcode opcode, byte[] buffer, int bufferIndex) {
        super(opcode, buffer, bufferIndex);
    }

    public Format getFormat() {
        return Format.Format32x;
    }

    public int getRegisterA() {
        return NumberUtils.decodeUnsignedShort(buffer, bufferIndex + 2);
    }

    public int getRegisterB() {
        return NumberUtils.decodeUnsignedShort(buffer, bufferIndex + 4);
    }

    private static class Factory implements Instruction.InstructionFactory {
        public Instruction makeInstruction(DexFile dexFile, Opcode opcode, byte[] buffer, int bufferIndex) {
            return new Instruction32x(opcode, buffer, bufferIndex);
        }
    }
}
