/*
 * Copyright (C) 2018 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ignis.backend.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.ignis.rpc.IRemoteException;

/**
 *
 * @author César Pomar
 */
public final class IgnisException extends IRemoteException {

    public IgnisException(String msg) {
        super(msg, "");
        setStack(stackToString(this));
    }

    public IgnisException(String msg, Throwable ex) {
        super(msg, stackToString(ex));
    }

    private static String stackToString(Throwable ex) {
        StringWriter writer = new StringWriter();
        ex.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
