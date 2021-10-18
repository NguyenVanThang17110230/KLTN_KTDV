import React from "react";
import UserDropdown from "../UserDropdown/UserDropdown";

const Header = () => {
  return (
    <nav className="w-full bg-transparent md:flex-row md:flex-nowrap md:justify-start flex items-center px-20 py-4 h-20 border-b border-gray-400">
      <div className="w-full h-full mx-auto items-center flex justify-between md:flex-nowrap flex-wrap">
        {/* Brand */}
        <a
          className="text-black text-sm uppercase lg:inline-block font-semibold"
          href="#pablo"
          onClick={(e) => e.preventDefault()}
        >
          Dashboard
        </a>
        {/* Form */}
        <form className="md:flex flex-row flex-wrap items-center lg:ml-auto mr-3">
          <div className="relative flex w-full flex-wrap items-stretch">
            <span className="z-10 h-full leading-snug font-normal absolute text-center text-blueGray-300 bg-transparent rounded text-base items-center justify-center w-8 pl-3 py-3">
              <i className="fas fa-search"></i>
            </span>
            <input
              type="text"
              placeholder="Search here..."
              className="border-0 px-3 py-3 placeholder-blueGray-300 text-blueGray-600 relative bg-white  rounded text-sm shadow outline-none focus:outline-none focus:ring w-full pl-10"
            />
          </div>
        </form>
        {/* User */}
        <ul className="flex-col md:flex-row list-none items-center md:flex relative">
          <UserDropdown />
        </ul>
      </div>
    </nav>
  );
};

export default Header;
