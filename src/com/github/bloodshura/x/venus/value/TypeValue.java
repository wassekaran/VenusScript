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

package com.github.bloodshura.x.venus.value;

import com.github.bloodshura.x.util.XApi;
import com.github.bloodshura.x.venus.type.PrimitiveType;
import com.github.bloodshura.x.venus.type.Type;

public class TypeValue extends Value {
	private final Type value;

	public TypeValue(Type value) {
		super(PrimitiveType.TYPE);
		XApi.requireNonNull(value, "value");

		this.value = value;
	}

	@Override
	public TypeValue clone() {
		return new TypeValue(value());
	}

	@Override
	public String toString() {
		return value().toString();
	}

	@Override
	public Type value() {
		return value;
	}
}