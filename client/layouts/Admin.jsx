import React, { FC, ReactNode } from "react";
import Sidebar from "../components/Sidebar/Sidebar";
import SidebarAdmin from "../components/Sidebar/SidebarAdmin"
import Header from "../containers/Header/Header";
import { Props } from "../package/Model";

export const AdminLayout = ({ children }) => {
  return (
    <>
      <div className="relative md:ml-64 bg-blueGray-100 h-screen">
        <SidebarAdmin />
        <Header />
        <div className="w-full">
          {children}
          {/* <FooterAdmin /> */}
        </div>
      </div>
    </>
  );
};
