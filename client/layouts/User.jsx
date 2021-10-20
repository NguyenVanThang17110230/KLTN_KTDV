import React from "react";
import Sidebar from "../components/Sidebar/Sidebar";
import Header from "../containers/Header/Header";

export const UserLayout = ({ children }) => {
  return (
    <>
      <div className="relative md:ml-64 bg-blueGray-100 h-full min-h-screen">
          <Sidebar />
          <Header />
          <div className="w-full">
            {children}
            {/* <FooterAdmin /> */}
          </div>
      </div>
    </>
  );
};
