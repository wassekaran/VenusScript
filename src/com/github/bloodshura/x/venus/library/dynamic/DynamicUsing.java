/*
 * Copyright (c) 2013-2018, João Vitor Verona Biazibetti - All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * https://www.github.com/BloodShura
 */

package com.github.bloodshura.x.venus.library.dynamic;

import com.github.bloodshura.x.venus.component.Script;
import com.github.bloodshura.x.venus.exception.runtime.ScriptRuntimeException;
import com.github.bloodshura.x.venus.executor.Context;
import com.github.bloodshura.x.venus.function.FunctionCallDescriptor;
import com.github.bloodshura.x.venus.function.VoidMethod;
import com.github.bloodshura.x.venus.function.annotation.MethodArgs;
import com.github.bloodshura.x.venus.function.annotation.MethodName;
import com.github.bloodshura.x.venus.library.VenusLibrary;
import com.github.bloodshura.x.venus.value.StringValue;

import java.util.function.Supplier;

@MethodArgs(StringValue.class)
@MethodName("dynamicUsing")
public class DynamicUsing extends VoidMethod {
	@Override
	public void callVoid(Context context, FunctionCallDescriptor descriptor) throws ScriptRuntimeException {
		StringValue libraryName = (StringValue) descriptor.get(0);
		Script script = context.getScript();
		Supplier<VenusLibrary> supplier = script.getApplicationContext().getLibrarySuppliers().get(libraryName.value());
		VenusLibrary library;

		if (supplier != null && (library = supplier.get()) != null) {
			script.getLibraryList().add(library);
		}
		else {
			throw new ScriptRuntimeException(context, "Could not find a library named \"" + libraryName + "\"");
		}
	}
}